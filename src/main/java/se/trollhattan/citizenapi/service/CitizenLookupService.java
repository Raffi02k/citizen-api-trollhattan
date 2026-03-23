package se.trollhattan.citizenapi.service;

import se.trollhattan.citizenapi.api.model.NavetPersonpostResponse;

public interface CitizenLookupService {
    NavetPersonpostResponse findByPersonNumber(String personNumber);
}
