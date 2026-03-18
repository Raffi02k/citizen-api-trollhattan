package se.trollhattan.citizenapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.trollhattan.citizenapi.entity.CitizenEntity;
import se.trollhattan.citizenapi.repository.CitizenRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CitizenServiceTest {

    private CitizenRepository citizenRepository;
    private CitizenService citizenService;

    @BeforeEach
    void setUp() {
        citizenRepository = mock(CitizenRepository.class);
    }

    @Test
    void shouldReturnPartyIdWhenCitizenExists() {
        CitizenEntity citizen = new CitizenEntity();
        citizen.setMunicipalityId("1488");
        citizen.setPersonNumber("199001011234");
        citizen.setPartyId("test-party-id-123");

        when(citizenRepository.findByMunicipalityIdAndPersonNumber("1488", "199001011234"))
                .thenReturn(Optional.of(citizen));

        String response = citizenService.getGuid("1488", "199001011234");

        assertEquals("test-party-id-123", response);
    }

    @Test
    void shouldCreatePartyIdWhenCitizenDoesNotExist() {
        when(citizenRepository.findByMunicipalityIdAndPersonNumber("1488", "111111111111"))
                .thenReturn(Optional.empty());
        when(citizenRepository.save(any(CitizenEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String response = citizenService.getGuid("1488", "111111111111");

        assertEquals(36, response.length());
    }

    @Test
    void shouldThrowExceptionWhenMunicipalityIdIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> citizenService.getGuid("", "199001011234"));
    }

    @Test
    void shouldThrowExceptionWhenPersonNumberIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> citizenService.getGuid("1488", ""));
    }
}
