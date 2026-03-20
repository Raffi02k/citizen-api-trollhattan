package se.trollhattan.citizenapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.trollhattan.citizenapi.api.model.NavetPersonpostResponse;
import se.trollhattan.citizenapi.entity.CitizenEntity;
import se.trollhattan.citizenapi.repository.CitizenRepository;
import se.trollhattan.citizenapi.exception.CitizenNotFoundException;

import java.util.UUID;

@Service
public class CitizenService {

    private final CitizenRepository citizenRepository;
    private final NavetService navetService;

    public CitizenService(CitizenRepository citizenRepository, NavetService navetService) {
        this.citizenRepository = citizenRepository;
        this.navetService = navetService;
    }

    public String getGuid(String municipalityId, String personNumber) {
        return getOrCreateGuid(municipalityId, personNumber);
    }

    @Transactional
    public String getOrCreateGuid(String municipalityId, String personNumber) {
        validateInput(municipalityId, personNumber);

        // 1. Look up in local database
        var existingCitizen = citizenRepository
                .findByMunicipalityIdAndPersonNumber(municipalityId, personNumber);

        // 2. If found -> return existing partyId
        if (existingCitizen.isPresent()) {
            return existingCitizen.get().getPartyId();
        }

        // 3. Call KIR
        NavetPersonpostResponse navetCitizen = navetService.findByPersonNumber(personNumber);

        // 4. If KIR confirms citizen exists -> create local citizen and return new
        // partyId
        if (navetCitizen != null) {
            String newPartyId = UUID.randomUUID().toString();

            CitizenEntity newCitizen = new CitizenEntity();
            newCitizen.setMunicipalityId(municipalityId);
            newCitizen.setPersonNumber(navetCitizen.getPersonNumber());
            newCitizen.setPartyId(newPartyId);

            citizenRepository.save(newCitizen);
            return newPartyId;
        }

        // 5. If not found in KIR -> throw error
        throw new CitizenNotFoundException(
                "Citizen was not found locally and was not found in KIR for personNumber=" + personNumber);
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
