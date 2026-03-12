package se.trollhattan.citizenapi.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.trollhattan.citizenapi.api.model.CitizenRequest;
import se.trollhattan.citizenapi.api.model.CitizenResponse;
import se.trollhattan.citizenapi.api.model.GuidResponse;
import se.trollhattan.citizenapi.service.CitizenService;

import java.util.UUID;

/**
 * REST controller for citizen-related endpoints.
 */
@RestController
public class CitizenController {

    private final CitizenService citizenService;

    /**
     * Creates a controller with the required service.
     *
     * @param citizenService the citizen service
     */
    public CitizenController(CitizenService citizenService) {
        this.citizenService = citizenService;
    }

    /**
     * Returns the partyId for the given municipality and person number.
     *
     * @param municipalityId the municipality identifier
     * @param personNumber   the Swedish personal identity number
     * @return the partyId wrapped in a GuidResponse
     */
    @GetMapping("/{municipalityId}/{personNumber}/guid")
    public ResponseEntity<GuidResponse> getGuid(
            @PathVariable String municipalityId,
            @PathVariable String personNumber) {

        // TODO Phase 2: Add person number format validation before calling the service
        GuidResponse response = citizenService.getGuidByPersonNumber(municipalityId, personNumber);
        return ResponseEntity.ok(response);
    }

    /**
     * Returns the citizen profile for the given partyId.
     *
     * @param municipalityId the municipality identifier
     * @param partyId        the stable UUID of the citizen
     * @return the citizen profile as a CitizenResponse
     */
    @GetMapping("/{municipalityId}/party/{partyId}")
    public ResponseEntity<CitizenResponse> getCitizenByPartyId(
            @PathVariable String municipalityId,
            @PathVariable UUID partyId) {

        CitizenResponse response = citizenService.getCitizenByPartyId(municipalityId, partyId);
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a citizen and returns the created record.
     *
     * @param municipalityId the municipality the citizen belongs to
     * @param request        the request body containing citizen data
     * @return the created citizen as a CitizenResponse
     */
    @PostMapping("/{municipalityId}")
    public ResponseEntity<CitizenResponse> createCitizen(
            @PathVariable String municipalityId,
            @Valid @RequestBody CitizenRequest request) {

        CitizenResponse response = citizenService.createCitizen(municipalityId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
