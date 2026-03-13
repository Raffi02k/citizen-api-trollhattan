package se.trollhattan.citizenapi.service;

import org.springframework.stereotype.Service;
import se.trollhattan.citizenapi.api.model.GuidResponse;
import se.trollhattan.citizenapi.entity.CitizenEntity;
import se.trollhattan.citizenapi.exception.CitizenNotFoundException;
import se.trollhattan.citizenapi.repository.CitizenRepository;

import java.util.UUID;

@Service
public class CitizenService {

    private final CitizenRepository citizenRepository;

    public CitizenService(CitizenRepository citizenRepository) {
        this.citizenRepository = citizenRepository;
    }

    public GuidResponse getGuid(String municipalityId, String personNumber) {
        validateInput(municipalityId, personNumber);

        CitizenEntity citizen = citizenRepository
                .findByMunicipalityIdAndPersonNumber(municipalityId, personNumber)
                .orElseThrow(() -> new CitizenNotFoundException(
                        "No citizen found for municipalityId=" + municipalityId
                                + " and personNumber=" + personNumber));

        return new GuidResponse(citizen.getPartyId());
    }

    public GuidResponse getOrCreateGuid(String municipalityId, String personNumber) {
        validateInput(municipalityId, personNumber);

        return citizenRepository
                .findByMunicipalityIdAndPersonNumber(municipalityId, personNumber)
                .map(citizen -> new GuidResponse(citizen.getPartyId()))
                .orElseGet(() -> {
                    String newPartyId = UUID.randomUUID().toString();

                    CitizenEntity newCitizen = new CitizenEntity();
                    newCitizen.setMunicipalityId(municipalityId);
                    newCitizen.setPersonNumber(personNumber);
                    newCitizen.setPartyId(newPartyId);

                    CitizenEntity savedCitizen = citizenRepository.save(newCitizen);

                    return new GuidResponse(savedCitizen.getPartyId());
                });
    }

    private void validateInput(String municipalityId, String personNumber) {
        if (municipalityId == null || municipalityId.isBlank()) {
            throw new IllegalArgumentException("municipalityId must not be blank");
        }

        if (personNumber == null || personNumber.isBlank()) {
            throw new IllegalArgumentException("personNumber must not be blank");
        }
    }
}