package se.trollhattan.citizenapi.service;

import org.junit.jupiter.api.Test;
import se.trollhattan.citizenapi.api.model.GuidResponse;
import se.trollhattan.citizenapi.exception.CitizenNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CitizenServiceTest {

    private final CitizenService citizenService = new CitizenService();

    @Test
    void shouldReturnPartyIdWhenCitizenExists() {
        GuidResponse response = citizenService.getGuid("1488", "199001011234");

        assertEquals("test-party-id-123", response.getPartyId());
    }

    @Test
    void shouldReturnSecondPartyIdWhenSecondCitizenExists() {
        GuidResponse response = citizenService.getGuid("1488", "198512121212");

        assertEquals("test-party-id-456", response.getPartyId());
    }

    @Test
    void shouldThrowExceptionWhenCitizenDoesNotExist() {
        assertThrows(CitizenNotFoundException.class, () -> citizenService.getGuid("1488", "111111111111"));
    }

    @Test
    void shouldThrowExceptionWhenMunicipalityIdIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> citizenService.getGuid("", "199001011234"));
    }

    @Test
    void shouldThrowExceptionWhenPersonNumberIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> citizenService.getGuid("1488", ""));
    }
}
