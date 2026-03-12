package se.trollhattan.citizenapi.service;

import org.springframework.stereotype.Service;
import se.trollhattan.citizenapi.api.model.GuidResponse;

@Service
public class CitizenService {

    public GuidResponse getGuid(String municipalityId, String personNumber) {
        if ("1488".equals(municipalityId) && "199001011234".equals(personNumber)) {
            return new GuidResponse("test-party-id-123");
        }

        if ("1488".equals(municipalityId) && "198512121212".equals(personNumber)) {
            return new GuidResponse("test-party-id-456");
        }

        return new GuidResponse("unknown-party-id");
    }
}