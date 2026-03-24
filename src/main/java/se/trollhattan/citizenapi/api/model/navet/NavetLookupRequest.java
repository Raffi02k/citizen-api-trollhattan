package se.trollhattan.citizenapi.api.model.navet;

import java.util.List;

/**
 * Request body for Navet's POST /folkbokforing/folkbokforingsuppgifter-for-offentliga-aktorer/v3/hamta.
 *
 * <p>Maps to Skatteverkets tjänstebeskrivning:
 * <pre>
 * {
 *   "bestallning": {
 *     "organisationsnummer": "...",
 *     "bestallningsidentitet": "..."
 *   },
 *   "sokvillkor": [
 *     { "identitetsbeteckning": "..." }
 *   ]
 * }
 * </pre>
 */
public record NavetLookupRequest(
        Bestallning bestallning,
        List<Sokvillkor> sokvillkor
) {

    public record Bestallning(
            String organisationsnummer,
            String bestallningsidentitet
    ) {
    }

    public record Sokvillkor(
            String identitetsbeteckning
    ) {
    }
}
