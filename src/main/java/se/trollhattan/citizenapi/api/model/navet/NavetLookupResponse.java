package se.trollhattan.citizenapi.api.model.navet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Optional;

/**
 * Top-level response from Navet's v3/hamta endpoint.
 *
 * <p>Navet returns either personposter (hits) or felrapporter (errors) per lookup.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record NavetLookupResponse(
        List<Personpost> personposter,
        List<Felrapport> felrapporter
) {

    /**
     * Returns the first successful person hit, if any.
     */
    public Optional<Personpost> firstHit() {
        if (personposter == null || personposter.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(personposter.get(0));
    }

    /**
     * Returns the first error report, if any.
     */
    public Optional<Felrapport> firstError() {
        if (felrapporter == null || felrapporter.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(felrapporter.get(0));
    }

    /**
     * A single person record returned by Navet.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Personpost(
            Identitet identitet,
            Namn namn,
            Boolean skyddAvPersonuppgifter
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Identitet(
            String identitetsbeteckning,
            Status status
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Status(
            String varde
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Namn(
            Fornamn fornamn,
            Efternamn efternamn
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Fornamn(
            String namn
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Efternamn(
            String namn
    ) {
    }

    /**
     * An error report returned by Navet when a lookup fails.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Felrapport(
            Integer orsakskod,
            String orsakskodsbeskrivning
    ) {
    }
}
