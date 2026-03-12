package se.trollhattan.citizenapi.service;

import org.springframework.stereotype.Service;
import se.trollhattan.citizenapi.api.model.GuidResponse;
import se.trollhattan.citizenapi.exception.CitizenNotFoundException;

@Service
public class CitizenService {

    public GuidResponse getGuid(String municipalityId, String personNumber) {

        if (municipalityId == null || municipalityId.isBlank()) {
            throw new IllegalArgumentException("municipalityId must not be blank");
        }

        if (personNumber == null || personNumber.isBlank()) {
            throw new IllegalArgumentException("personNumber must not be blank");
        }

        if ("1488".equals(municipalityId) && "199001011234".equals(personNumber)) {
            return new GuidResponse("test-party-id-123");
        }

        if ("1488".equals(municipalityId) && "198512121212".equals(personNumber)) {
            return new GuidResponse("test-party-id-456");
        }

        throw new CitizenNotFoundException(
                "No citizen found for municipalityId=" + municipalityId + " and personNumber=" + personNumber);
    }
}