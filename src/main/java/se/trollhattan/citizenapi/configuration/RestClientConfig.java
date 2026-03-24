package se.trollhattan.citizenapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient.Builder restClientBuilder(NavetProperties navetProperties) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(navetProperties.getConnectTimeoutMs());
        requestFactory.setReadTimeout(navetProperties.getReadTimeoutMs());

        return RestClient.builder()
                .requestFactory(requestFactory);
    }
}
