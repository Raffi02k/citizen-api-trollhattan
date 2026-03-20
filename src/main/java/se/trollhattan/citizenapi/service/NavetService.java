package se.trollhattan.citizenapi.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import se.trollhattan.citizenapi.configuration.NavetProperties;
import se.trollhattan.citizenapi.api.model.NavetPersonpostResponse;

@Service
public class NavetService {

    private final RestClient restClient;
    private final NavetProperties kirProperties;

    public NavetService(RestClient.Builder restClientBuilder, NavetProperties kirProperties) {
        this.kirProperties = kirProperties;
        this.restClient = restClientBuilder
                .baseUrl(kirProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("x-api-key", kirProperties.getApiKey())
                .build();
    }

    public NavetPersonpostResponse findByPersonNumber(String personNumber) {
        String path = kirProperties.getPersonLookupPath()
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
