package se.trollhattan.citizenapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.trollhattan.citizenapi.entity.CitizenEntity;
import se.trollhattan.citizenapi.repository.CitizenRepository;
import se.trollhattan.citizenapi.exception.CitizenNotFoundException;

import java.util.UUID;

@Service
public class CitizenService {

    private final CitizenRepository citizenRepository;
    private final KirService kirService;

    public CitizenService(CitizenRepository citizenRepository, KirService kirService) {
        this.citizenRepository = citizenRepository;
        this.kirService = kirService;
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
        boolean existsInKir = kirService.existsByPersonNumber(personNumber);

        // 4. If KIR confirms citizen exists -> create local citizen and return new
        // partyId
        if (existsInKir) {
            String newPartyId = UUID.randomUUID().toString();

            CitizenEntity newCitizen = new CitizenEntity();
            newCitizen.setMunicipalityId(municipalityId);
            newCitizen.setPersonNumber(personNumber);
            newCitizen.setPartyId(newPartyId);

            CitizenEntity savedCitizen = citizenRepository.save(newCitizen);
            return savedCitizen.getPartyId();
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
