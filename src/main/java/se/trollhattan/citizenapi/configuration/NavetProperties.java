package se.trollhattan.citizenapi.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "navet")
public class NavetProperties {

    private String baseUrl;
    private String apiKey;
    private int connectTimeoutMs;
    private int readTimeoutMs;
    private String personLookupPath;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public void setConnectTimeoutMs(int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public void setReadTimeoutMs(int readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }

    public String getPersonLookupPath() {
        return personLookupPath;
    }

    public void setPersonLookupPath(String personLookupPath) {
        this.personLookupPath = personLookupPath;
    }
}
