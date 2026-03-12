package se.trollhattan.citizenapi.service;

import org.springframework.stereotype.Service;
import se.trollhattan.citizenapi.api.model.GuidResponse;

@Service
public class CitizenService {

    public GuidResponse getGuid(String municipalityId, String personNumber) {
        return new GuidResponse("test-party-id-123");
    }
}