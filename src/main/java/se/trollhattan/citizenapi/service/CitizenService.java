package se.trollhattan.citizenapi.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.trollhattan.citizenapi.api.model.citizen.CitizenLookupResult;
import se.trollhattan.citizenapi.entity.CitizenEntity;
import se.trollhattan.citizenapi.exception.CitizenNotFoundException;
import se.trollhattan.citizenapi.exception.NavetUnavailableException;
import se.trollhattan.citizenapi.repository.CitizenRepository;
import se.trollhattan.citizenapi.util.PersonNumberValidator;

import java.util.UUID;

@Service
public class CitizenService {

    private final CitizenRepository citizenRepository;
    private final CitizenLookupService citizenLookupService;
    private final PersonNumberValidator personNumberValidator;

    public CitizenService(
            CitizenRepository citizenRepository,
            CitizenLookupService citizenLookupService,
            PersonNumberValidator personNumberValidator) {
        this.citizenRepository = citizenRepository;
        this.citizenLookupService = citizenLookupService;
        this.personNumberValidator = personNumberValidator;
    }

    public String getGuid(String municipalityId, String personNumber) {
        return getOrCreateGuid(municipalityId, personNumber);
    }

    @Transactional
    public String getOrCreateGuid(String municipalityId, String personNumber) {
        validateInput(municipalityId, personNumber);

        var existingCitizen = citizenRepository
                .findByMunicipalityIdAndPersonNumber(municipalityId, personNumber);

        if (existingCitizen.isPresent()) {
            return existingCitizen.get().getPartyId();
        }

        CitizenLookupResult lookupResult = citizenLookupService.findByPersonNumber(personNumber);

        if (lookupResult == null || lookupResult.personNumber() == null || lookupResult.personNumber().isBlank()) {
            throw new CitizenNotFoundException(
                    "Citizen was not found locally and Navet returned no valid person for personNumber="
                            + personNumber);
        }

        String newPartyId = UUID.randomUUID().toString();

        CitizenEntity newCitizen = new CitizenEntity();
        newCitizen.setMunicipalityId(municipalityId);
        newCitizen.setPersonNumber(lookupResult.personNumber());
        newCitizen.setPartyId(newPartyId);

        try {
            citizenRepository.saveAndFlush(newCitizen);
            return newPartyId;
        } catch (DataIntegrityViolationException ex) {
            // Race condition: another request saved the same citizen concurrently; return
            // their party ID
            return citizenRepository.findByMunicipalityIdAndPersonNumber(municipalityId, personNumber)
                    .map(CitizenEntity::getPartyId)
                    .orElseThrow(() -> new NavetUnavailableException(
                            "Citizen skapades parallellt men kunde inte läsas tillbaka", ex));
        }
    }

    private void validateInput(String municipalityId, String personNumber) {
        if (municipalityId == null || municipalityId.isBlank()) {
            throw new IllegalArgumentException("municipalityId must not be blank");
        }

        if (!municipalityId.matches("\\d+")) {
            throw new IllegalArgumentException("municipalityId must be numeric");
        }

        if (personNumber == null || personNumber.isBlank()) {
            throw new IllegalArgumentException("personNumber must not be blank");
        }

        if (!personNumberValidator.isValid(personNumber)) {
            throw new IllegalArgumentException(
                    "personNumber must be a valid Swedish personal identity number in 12-digit format");
        }
    }
}
