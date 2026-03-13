package se.trollhattan.citizenapi.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import se.trollhattan.citizenapi.api.CitizenController;
import se.trollhattan.citizenapi.api.model.GuidResponse;
import se.trollhattan.citizenapi.exception.CitizenNotFoundException;
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
                .thenReturn(new GuidResponse("test-party-id-123"));

        mockMvc.perform(get("/1488/199001011234/guid"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.partyId").value("test-party-id-123"));
    }

    @Test
    @DisplayName("Should return 404 when citizen does not exist")
    void shouldReturn404WhenCitizenDoesNotExist() throws Exception {
        when(citizenService.getGuid("1488", "111111111111"))
                .thenThrow(new CitizenNotFoundException(
                        "No citizen found for municipalityId=1488 and personNumber=111111111111"));

        mockMvc.perform(get("/1488/111111111111/guid"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
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
