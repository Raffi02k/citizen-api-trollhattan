package se.trollhattan.citizenapi.service;

import org.springframework.stereotype.Service;
import se.trollhattan.citizenapi.api.model.CitizenRequest;
import se.trollhattan.citizenapi.api.model.CitizenResponse;
import se.trollhattan.citizenapi.api.model.GuidResponse;
import se.trollhattan.citizenapi.integration.db.CitizenRepository;
import se.trollhattan.citizenapi.service.mapper.CitizenMapper;

import java.util.UUID;

/**
 * Service for citizen-related operations and business rules.
 */
@Service
public class CitizenService {

    private final CitizenRepository citizenRepository;
    private final CitizenMapper citizenMapper;

    /**
     * Creates a service with the required repository and mapper.
     *
     * @param citizenRepository the citizen repository
     * @param citizenMapper     the citizen mapper
     */
    public CitizenService(CitizenRepository citizenRepository, CitizenMapper citizenMapper) {
        this.citizenRepository = citizenRepository;
        this.citizenMapper = citizenMapper;
    }

    /**
     * Returns the partyId for a citizen in the given municipality.
     *
     * @param municipalityId the municipality identifier
     * @param personNumber   the Swedish personal identity number
     * @return the partyId wrapped in a GuidResponse
     */
    public GuidResponse getGuidByPersonNumber(String municipalityId, String personNumber) {
        // TODO Phase 2: Implement the lookup logic
        // 1. Call citizenRepository.findByMunicipalityIdAndPersonNumber(...)
        // 2. If present, use citizenMapper.toGuidResponse(entity)
        // 3. If not found, throw new CitizenNotFoundException(...)

        return null; // Placeholder — will be implemented in Phase 2
    }

    /**
     * Returns the citizen profile for the given partyId.
     *
     * @param municipalityId the municipality identifier
     * @param partyId        the stable UUID of the citizen
     * @return the citizen profile as a CitizenResponse
     */
    public CitizenResponse getCitizenByPartyId(String municipalityId, UUID partyId) {
        // TODO Phase 2: Implement the retrieval logic
        // 1. Call citizenRepository.findByMunicipalityIdAndPartyId(...)
        // 2. If present, use citizenMapper.toResponse(entity)
        // 3. If not found, throw new CitizenNotFoundException(...)

        return null; // Placeholder — will be implemented in Phase 2
    }

    /**
     * Creates a citizen record and returns the result.
     *
     * @param municipalityId the municipality the citizen belongs to
     * @param request        the request body containing the person number
     * @return the created or existing citizen as a CitizenResponse
     */
    public CitizenResponse createCitizen(String municipalityId, CitizenRequest request) {
        // TODO Phase 2: Implement the creation logic
        // 1. Check citizenRepository.findByMunicipalityIdAndPersonNumber(...)
        // 2. If exists, return citizenMapper.toResponse(existingEntity)
        // 3. If not, create entity via citizenMapper.toEntity(...)
        // 4. Save via citizenRepository.save(newEntity)
        // 5. Return citizenMapper.toResponse(savedEntity)

        return null; // Placeholder — will be implemented in Phase 2
    }
}
