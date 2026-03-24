package se.trollhattan.citizenapi.service;

import se.trollhattan.citizenapi.api.model.citizen.CitizenLookupResult;

/**
 * Abstraction for looking up a citizen by personal identity number in an
 * external source.
 * Currently implemented by {@link NavetService}.
 */
public interface CitizenLookupService {
    CitizenLookupResult findByPersonNumber(String personNumber);
}
