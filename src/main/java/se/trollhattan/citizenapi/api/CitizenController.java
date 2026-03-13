package se.trollhattan.citizenapi.api;

import org.springframework.web.bind.annotation.*;
import se.trollhattan.citizenapi.api.model.GuidResponse;
import se.trollhattan.citizenapi.service.CitizenService;

@RestController
@RequestMapping
public class CitizenController {

    private final CitizenService citizenService;

    public CitizenController(CitizenService citizenService) {
        this.citizenService = citizenService;
    }

    @GetMapping("/{municipalityId}/{personNumber}/guid")
    public GuidResponse getGuid(
            @PathVariable String municipalityId,
            @PathVariable String personNumber) {
        return citizenService.getGuid(municipalityId, personNumber);
    }

    @PostMapping("/{municipalityId}/{personNumber}/guid")
    public GuidResponse getOrCreateGuid(
            @PathVariable String municipalityId,
            @PathVariable String personNumber) {
        return citizenService.getOrCreateGuid(municipalityId, personNumber);
    }
}