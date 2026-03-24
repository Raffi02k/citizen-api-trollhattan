package se.trollhattan.citizenapi.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "navet")
public class NavetProperties {

    private String baseUrl;
    private String lookupPath;
    private int connectTimeoutMs;
    private int readTimeoutMs;
    private final Order order = new Order();
    private final OAuth oauth = new OAuth();
    private final ApiGateway apiGateway = new ApiGateway();

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getLookupPath() {
        return lookupPath;
    }

    public void setLookupPath(String lookupPath) {
        this.lookupPath = lookupPath;
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

    public Order getOrder() {
        return order;
    }

    public OAuth getOauth() {
        return oauth;
    }

    public ApiGateway getApiGateway() {
        return apiGateway;
    }

    public static class Order {
        private String organisationsnummer;
        private String bestallningsidentitet;

        public String getOrganisationsnummer() {
            return organisationsnummer;
        }

        public void setOrganisationsnummer(String organisationsnummer) {
            this.organisationsnummer = organisationsnummer;
        }

        public String getBestallningsidentitet() {
            return bestallningsidentitet;
        }

        public void setBestallningsidentitet(String bestallningsidentitet) {
            this.bestallningsidentitet = bestallningsidentitet;
        }
    }

    public static class OAuth {
        private String tokenUrl;
        private String clientId;
        private String clientSecret;
        private String scope;

        public String getTokenUrl() {
            return tokenUrl;
        }

        public void setTokenUrl(String tokenUrl) {
            this.tokenUrl = tokenUrl;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
    }

    public static class ApiGateway {
        private String clientId;
        private String clientSecret;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }
    }
}
