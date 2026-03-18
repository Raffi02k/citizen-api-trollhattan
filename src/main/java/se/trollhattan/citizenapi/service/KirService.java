package se.trollhattan.citizenapi.service;

import org.springframework.stereotype.Service;

/**
 * Temporary service for KIR lookup.
 *
 * Right now this class is only a placeholder so we can build the
 * Citizen API flow before we know the real KIR integration details.
 *
 * After the meeting about KIR, this class can be updated to call
 * the real KIR API or other technical integration point.
 */
@Service
public class KirService {

    public boolean existsByPersonNumber(String personNumber) {
        // Temporary fake KIR logic for testing
        return "199001011234".equals(personNumber)
                || "198512121212".equals(personNumber)
                || "200001011234".equals(personNumber);

    }
}
