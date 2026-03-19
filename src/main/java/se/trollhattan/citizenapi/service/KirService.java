package se.trollhattan.citizenapi.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import se.trollhattan.citizenapi.configuration.KirProperties;
import se.trollhattan.citizenapi.api.model.KirCitizenResponse;

@Service
public class KirService {

    private final RestClient restClient;
    private final KirProperties kirProperties;

    public KirService(RestClient.Builder restClientBuilder, KirProperties kirProperties) {
        this.kirProperties = kirProperties;
        this.restClient = restClientBuilder
                .baseUrl(kirProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("x-api-key", kirProperties.getApiKey())
                .build();
    }

    public KirCitizenResponse findByPersonNumber(String personNumber) {
        String path = kirProperties.getPersonLookupPath()
                .replace("{personNumber}", personNumber);

        return restClient.get()
                .uri(path)
                .retrieve()
                .body(KirCitizenResponse.class);
    }

    public boolean existsByPersonNumber(String personNumber) {
        try {
            KirCitizenResponse response = findByPersonNumber(personNumber);
            return response != null && response.getPersonNumber() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
