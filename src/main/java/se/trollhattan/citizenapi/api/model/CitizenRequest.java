package se.trollhattan.citizenapi.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request body for creating a citizen.
 *
 * @param personNumber the Swedish personal identity number (12 digits)
 */
public record CitizenRequest(

        @NotBlank(message = "Person number must not be blank") @Pattern(regexp = "\\d{12}", message = "Person number must be exactly 12 digits (YYYYMMDDNNNN)") String personNumber) {
}
