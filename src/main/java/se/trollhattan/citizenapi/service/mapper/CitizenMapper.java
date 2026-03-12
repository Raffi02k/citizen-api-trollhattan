package se.trollhattan.citizenapi.service.mapper;

import org.springframework.stereotype.Component;
import se.trollhattan.citizenapi.api.model.CitizenRequest;
import se.trollhattan.citizenapi.api.model.CitizenResponse;
import se.trollhattan.citizenapi.api.model.GuidResponse;
import se.trollhattan.citizenapi.integration.db.model.CitizenEntity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Maps between database entities and API DTOs.
 */
@Component
public class CitizenMapper {

    /**
     * Converts a citizen entity to a full response.
     *
     * @param entity the citizen entity from the database
     * @return the CitizenResponse DTO
     */
    public CitizenResponse toResponse(CitizenEntity entity) {
        // TODO Phase 2: Implement the actual mapping
        // return new CitizenResponse(
        // entity.getMunicipalityId(),
        // entity.getPersonNumber(),
        // entity.getPartyId()
        // );

        return null; // Placeholder — will be implemented in Phase 2
    }

    /**
     * Converts a citizen entity to a GUID-only response.
     *
     * @param entity the citizen entity from the database
     * @return a GuidResponse containing only the partyId
     */
    public GuidResponse toGuidResponse(CitizenEntity entity) {
        // TODO Phase 2: Implement the actual mapping
        // return new GuidResponse(entity.getPartyId());

        return null; // Placeholder — will be implemented in Phase 2
    }

    /**
     * Creates a new entity from a request.
     *
     * @param municipalityId the municipality the citizen belongs to
     * @param request        the incoming request body with person number
     * @return a new CitizenEntity ready to be saved
     */
    public CitizenEntity toEntity(String municipalityId, CitizenRequest request) {
        // TODO Phase 2: Implement the actual mapping
        // return CitizenEntity.builder()
        // .municipalityId(municipalityId)
        // .personNumber(request.personNumber())
        // .partyId(UUID.randomUUID())
        // .createdAt(LocalDateTime.now())
        // .build();

        return null; // Placeholder — will be implemented in Phase 2
    }
}
