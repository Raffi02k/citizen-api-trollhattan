package se.trollhattan.citizenapi.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import se.trollhattan.citizenapi.api.model.GuidResponse;
import se.trollhattan.citizenapi.service.CitizenService;

/**
 * REST controller for citizen-related endpoints.
 */
@RestController
public class CitizenController {

    private final CitizenService citizenService;

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
    public GuidResponse getGuid(
            @PathVariable String municipalityId,
            @PathVariable String personNumber) {

        return citizenService.getGuid(municipalityId, personNumber);
    }
}