package se.trollhattan.citizenapi.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import se.trollhattan.citizenapi.api.model.NavetPersonpostResponse;
import se.trollhattan.citizenapi.configuration.NavetProperties;

@Service
public class NavetService implements CitizenLookupService {

    private final RestClient restClient;
    private final NavetProperties navetProperties;

    public NavetService(RestClient.Builder restClientBuilder, NavetProperties navetProperties) {
        this.navetProperties = navetProperties;
        this.restClient = restClientBuilder
                .baseUrl(navetProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                // Lägg bara till headers som faktiskt gäller för er lösning:
                // .defaultHeader("x-api-key", navetProperties.getApiKey())
                // .defaultHeader("X-Client-Id", navetProperties.getClientId())
                .build();
    }

    public NavetPersonpostResponse findByPersonNumber(String personNumber) {
        String path = navetProperties.getPersonLookupPath()
                .replace("{personNumber}", personNumber);

        return restClient.get()
                .uri(path)
                .retrieve()
                .body(NavetPersonpostResponse.class);
    }

    public boolean existsByPersonNumber(String personNumber) {
        try {
            NavetPersonpostResponse response = findByPersonNumber(personNumber);
            return response != null && response.getPersonNumber() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
