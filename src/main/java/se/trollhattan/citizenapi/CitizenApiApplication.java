package se.trollhattan.citizenapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Application entry point for the Citizen API.
 */
@ConfigurationPropertiesScan
@SpringBootApplication
public class CitizenApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CitizenApiApplication.class, args);
    }

}
