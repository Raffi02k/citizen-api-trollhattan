package se.trollhattan.citizenapi.service;

import org.springframework.stereotype.Service;
import se.trollhattan.citizenapi.entity.CitizenEntity;
import se.trollhattan.citizenapi.repository.CitizenRepository;

import java.util.UUID;

@Service
public class CitizenService {

    private final CitizenRepository citizenRepository;

    public CitizenService(CitizenRepository citizenRepository) {
        this.citizenRepository = citizenRepository;
    }

    public String getGuid(String municipalityId, String personNumber) {
        return getOrCreateGuid(municipalityId, personNumber);
    }

    public String getOrCreateGuid(String municipalityId, String personNumber) {
        validateInput(municipalityId, personNumber);

        return citizenRepository
                .findByMunicipalityIdAndPersonNumber(municipalityId, personNumber)
                .map(CitizenEntity::getPartyId)
                .orElseGet(() -> {
                    String newPartyId = UUID.randomUUID().toString();

                    CitizenEntity newCitizen = new CitizenEntity();
                    newCitizen.setMunicipalityId(municipalityId);
                    newCitizen.setPersonNumber(personNumber);
                    newCitizen.setPartyId(newPartyId);

                    CitizenEntity savedCitizen = citizenRepository.save(newCitizen);

                    return savedCitizen.getPartyId();
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
