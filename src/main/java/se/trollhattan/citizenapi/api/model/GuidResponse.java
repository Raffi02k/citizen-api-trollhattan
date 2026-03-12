package se.trollhattan.citizenapi.api.model;

import java.util.UUID;

/**
 * Response body containing only a citizen's partyId.
 *
 * @param partyId the stable UUID/GUID assigned to the citizen
 */
public record GuidResponse(
        UUID partyId) {
}
