package se.trollhattan.citizenapi.api.model.citizen;

import java.util.UUID;

/**
 * Response body for a full citizen profile.
 *
 * @param municipalityId the municipality the citizen belongs to
 * @param personNumber   the citizen's personal identity number
 * @param partyId        the stable UUID/GUID assigned to the citizen
 */
public record CitizenResponse(
                String municipalityId,
                String personNumber,
                UUID partyId) {
}
