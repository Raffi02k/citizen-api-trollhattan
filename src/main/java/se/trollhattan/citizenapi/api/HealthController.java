package se.trollhattan.citizenapi.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for health checks.
 */
@RestController
public class HealthController {

    /**
     * Returns a simple message confirming the API is running.
     *
     * @return a plain text health status message
     */
    @GetMapping("/health")
    public String health() {
        return "Citizen API is running";
    }
}
