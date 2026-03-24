package se.trollhattan.citizenapi.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import se.trollhattan.citizenapi.configuration.NavetProperties;
import se.trollhattan.citizenapi.exception.NavetUnauthorizedException;
import se.trollhattan.citizenapi.exception.NavetUnavailableException;

import java.time.Instant;

/**
 * Fetches and caches OAuth 2.0 access tokens for Navet using the Client Credentials Grant (CCG).
 *
 * <p>Skatteverket's Navet REST API requires a Bearer token obtained via OAuth 2.0 CCG.
 * This service caches the token until it is about to expire (with a 30-second buffer)
 * to avoid unnecessary token requests on every Navet call.
 */
@Service
public class NavetTokenService {

    /**
     * Number of seconds before expiry to consider the token "about to expire" and refresh it proactively.
     */
    private static final int EXPIRY_BUFFER_SECONDS = 30;

    private final RestClient tokenRestClient;
    private final NavetProperties navetProperties;

    private volatile String cachedToken;
    private volatile Instant tokenExpiresAt;

    public NavetTokenService(RestClient.Builder restClientBuilder, NavetProperties navetProperties) {
        this.navetProperties = navetProperties;
        // Use a separate, plain RestClient for the token endpoint (no base URL needed at build time)
        this.tokenRestClient = restClientBuilder.build();
    }

    /**
     * Returns a valid access token, fetching a new one if none is cached or the cached one is about to expire.
     *
     * @return a valid Bearer token string
     * @throws NavetUnauthorizedException if the token endpoint rejects the credentials
     * @throws NavetUnavailableException  if the token endpoint cannot be reached
     */
    public String getAccessToken() {
        if (cachedToken != null && tokenExpiresAt != null
                && Instant.now().isBefore(tokenExpiresAt.minusSeconds(EXPIRY_BUFFER_SECONDS))) {
            return cachedToken;
        }
        return fetchNewToken();
    }

    private synchronized String fetchNewToken() {
        // Double-check inside synchronized block in case another thread already refreshed
        if (cachedToken != null && tokenExpiresAt != null
                && Instant.now().isBefore(tokenExpiresAt.minusSeconds(EXPIRY_BUFFER_SECONDS))) {
            return cachedToken;
        }

        NavetProperties.OAuth oauth = navetProperties.getOauth();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", oauth.getClientId());
        formData.add("client_secret", oauth.getClientSecret());
        if (oauth.getScope() != null && !oauth.getScope().isBlank()) {
            formData.add("scope", oauth.getScope());
        }

        try {
            TokenResponse tokenResponse = tokenRestClient.post()
                    .uri(oauth.getTokenUrl())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .body(TokenResponse.class);

            if (tokenResponse == null || tokenResponse.accessToken() == null) {
                throw new NavetUnauthorizedException("Token-tjänsten svarade utan access token");
            }

            cachedToken = tokenResponse.accessToken();
            long expiresIn = tokenResponse.expiresIn() != null ? tokenResponse.expiresIn() : 3600L;
            tokenExpiresAt = Instant.now().plusSeconds(expiresIn);

            return cachedToken;
        } catch (NavetUnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new NavetUnavailableException("Kunde inte hämta access token från Navet OAuth-tjänst", e);
        }
    }

    /**
     * Internal record for deserializing the OAuth token response.
     */
    private record TokenResponse(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("expires_in") Long expiresIn,
            @JsonProperty("token_type") String tokenType
    ) {
    }
}
