package se.trollhattan.citizenapi.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import se.trollhattan.citizenapi.api.CitizenController;
import se.trollhattan.citizenapi.service.CitizenService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Web layer test for CitizenController.
 */
@WebMvcTest(CitizenController.class)
class CitizenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CitizenService citizenService;

    @Test
    @DisplayName("Should return 200 and partyId when citizen exists")
    void shouldReturnPartyIdWhenCitizenExists() throws Exception {
        when(citizenService.getGuid("1488", "199001011234"))
                .thenReturn("test-party-id-123");

        mockMvc.perform(get("/1488/199001011234/guid"))
                .andExpect(status().isOk())
                .andExpect(content().string("test-party-id-123"));
    }

    @Test
    @DisplayName("Should return 200 and partyId when citizen does not exist")
    void shouldReturnPartyIdWhenCitizenDoesNotExist() throws Exception {
        when(citizenService.getGuid("1488", "111111111111"))
                .thenReturn("generated-party-id-456");

        mockMvc.perform(get("/1488/111111111111/guid"))
                .andExpect(status().isOk())
                .andExpect(content().string("generated-party-id-456"));
    }

    @Test
    @DisplayName("Should return 400 when municipalityId is invalid")
    void shouldReturn400WhenMunicipalityIdIsInvalid() throws Exception {
        when(citizenService.getGuid("bad", "199001011234"))
                .thenThrow(new IllegalArgumentException("municipalityId must be numeric"));

        mockMvc.perform(get("/bad/199001011234/guid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("municipalityId must be numeric"));
    }
}
