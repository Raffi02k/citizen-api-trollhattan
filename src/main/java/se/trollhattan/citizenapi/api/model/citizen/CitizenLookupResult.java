package se.trollhattan.citizenapi.api.model.citizen;

/**
 * Normalized result from a citizen lookup, decoupled from Navet's raw response
 * format.
 *
 * @param personNumber           The 12-digit Swedish personal identity number
 *                               (identitetsbeteckning).
 * @param firstName              First name, or null if not available.
 * @param lastName               Last name, or null if not available.
 * @param sourceStatus           Navet's status code for the person record (e.g.
 *                               "Folkbokförd"), or null.
 * @param skyddAvPersonuppgifter True if the person has protected identity
 *                               (sekretessmarkering).
 */
public record CitizenLookupResult(
                String personNumber,
                String firstName,
                String lastName,
                String sourceStatus,
                Boolean skyddAvPersonuppgifter) {
}
