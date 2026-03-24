package se.trollhattan.citizenapi.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import se.trollhattan.citizenapi.api.model.citizen.CitizenLookupResult;
import se.trollhattan.citizenapi.api.model.navet.NavetLookupRequest;
import se.trollhattan.citizenapi.api.model.navet.NavetLookupResponse;
import se.trollhattan.citizenapi.configuration.NavetProperties;
import se.trollhattan.citizenapi.exception.CitizenNotFoundException;
import se.trollhattan.citizenapi.exception.NavetBadRequestException;
import se.trollhattan.citizenapi.exception.NavetRateLimitException;
import se.trollhattan.citizenapi.exception.NavetUnauthorizedException;
import se.trollhattan.citizenapi.exception.NavetUnavailableException;

import java.util.List;
import java.util.UUID;

@Service
public class NavetService implements CitizenLookupService {

    private static final String CORRELATION_HEADER = "skv_client_correlation_id";
    private static final String API_GW_CLIENT_ID_HEADER = "client_id";
    private static final String API_GW_CLIENT_SECRET_HEADER = "client_secret";

    private final RestClient restClient;
    private final NavetProperties navetProperties;
    private final NavetTokenService navetTokenService;

    public NavetService(
            RestClient.Builder restClientBuilder,
            NavetProperties navetProperties,
            NavetTokenService navetTokenService) {
        this.navetProperties = navetProperties;
        this.navetTokenService = navetTokenService;
        this.restClient = restClientBuilder
                .baseUrl(navetProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public CitizenLookupResult findByPersonNumber(String personNumber) {
        requireConfigured("navet.base-url", navetProperties.getBaseUrl());
        requireConfigured("navet.lookup-path", navetProperties.getLookupPath());
        requireConfigured("navet.order.organisationsnummer", navetProperties.getOrder().getOrganisationsnummer());
        requireConfigured("navet.order.bestallningsidentitet", navetProperties.getOrder().getBestallningsidentitet());
        requireConfigured("navet.api-gateway.client-id", navetProperties.getApiGateway().getClientId());
        requireConfigured("navet.api-gateway.client-secret", navetProperties.getApiGateway().getClientSecret());

        NavetLookupRequest request = new NavetLookupRequest(
                new NavetLookupRequest.Bestallning(
                        navetProperties.getOrder().getOrganisationsnummer(),
                        navetProperties.getOrder().getBestallningsidentitet()),
                List.of(new NavetLookupRequest.Sokvillkor(personNumber)));

        String accessToken = navetTokenService.getAccessToken();
        String correlationId = UUID.randomUUID().toString();

        try {
            NavetLookupResponse response = restClient.post()
                    .uri(navetProperties.getLookupPath())
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(headers -> {
                        headers.setBearerAuth(accessToken);
                        headers.set(API_GW_CLIENT_ID_HEADER, navetProperties.getApiGateway().getClientId());
                        headers.set(API_GW_CLIENT_SECRET_HEADER, navetProperties.getApiGateway().getClientSecret());
                        headers.set(CORRELATION_HEADER, correlationId);
                    })
                    .body(request)
                    .retrieve()
                    .body(NavetLookupResponse.class);

            return mapToLookupResult(personNumber, response);
        } catch (RestClientResponseException ex) {
            throw mapHttpException(ex);
        } catch (ResourceAccessException ex) {
            throw new NavetUnavailableException("Timeout eller nätverksfel mot Navet", ex);
        }
    }

    private CitizenLookupResult mapToLookupResult(String requestedPersonNumber, NavetLookupResponse response) {
        if (response == null) {
            throw new NavetUnavailableException("Navet svarade utan body");
        }

        var hit = response.firstHit();
        if (hit.isPresent() && hit.get().identitet() != null
                && StringUtils.hasText(hit.get().identitet().identitetsbeteckning())) {
            String sourceStatus = hit.get().identitet().status() != null
                    ? hit.get().identitet().status().varde()
                    : null;
            String firstName = hit.get().namn() != null && hit.get().namn().fornamn() != null
                    ? hit.get().namn().fornamn().namn()
                    : null;
            String lastName = hit.get().namn() != null && hit.get().namn().efternamn() != null
                    ? hit.get().namn().efternamn().namn()
                    : null;

            return new CitizenLookupResult(
                    hit.get().identitet().identitetsbeteckning(),
                    firstName,
                    lastName,
                    sourceStatus,
                    hit.get().skyddAvPersonuppgifter());
        }

        var error = response.firstError();
        if (error.isPresent()) {
            Integer code = error.get().orsakskod();
            String message = error.get().orsakskodsbeskrivning();
            if (code != null && code == 404) {
                throw new CitizenNotFoundException("Personen finns inte i Navet: " + requestedPersonNumber);
            }
            if (code != null && code == 400) {
                throw new NavetBadRequestException("Navet avvisade sökningen: " + message);
            }
            throw new NavetUnavailableException("Navet returnerade affärsfel: " + message);
        }

        throw new CitizenNotFoundException("Ingen träff i Navet för personnummer: " + requestedPersonNumber);
    }

    private RuntimeException mapHttpException(RestClientResponseException ex) {
        int status = ex.getStatusCode().value();
        return switch (status) {
            case 400 -> new NavetBadRequestException("Navet avvisade anropet", ex);
            case 401, 403 -> new NavetUnauthorizedException("Navet nekade åtkomst eller token", ex);
            case 429 -> new NavetRateLimitException("Navet rate limit uppnådd", ex);
            case 500, 502, 503, 504 -> new NavetUnavailableException("Navet är inte tillgängligt just nu", ex);
            default -> new NavetUnavailableException("Oväntat fel från Navet: HTTP " + status, ex);
        };
    }

    private void requireConfigured(String key, String value) {
        if (!StringUtils.hasText(value)) {
            throw new NavetUnavailableException("Saknad konfiguration: " + key);
        }
    }
}
