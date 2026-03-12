package se.trollhattan.citizenapi.util;

import org.springframework.stereotype.Component;

/**
 * Utility for validating Swedish personal identity numbers (personnummer).
 */
@Component
public class PersonNumberValidator {

    /**
     * Checks whether the person number is in the expected 12-digit format.
     *
     * @param personNumber the person number string to validate
     * @return true if the format is valid, false otherwise
     */
    public boolean isValid(String personNumber) {
        // TODO Phase 2: Implement full validation
        // Step 1: Check length and digit-only
        // Step 2: Parse and validate the date (YYYYMMDD)
        // Step 3: Calculate and verify Luhn checksum

        // Placeholder: basic format check only
        return personNumber != null && personNumber.matches("\\d{12}");
    }
}
