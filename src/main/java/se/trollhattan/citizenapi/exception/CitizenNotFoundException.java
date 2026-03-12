package se.trollhattan.citizenapi.exception;

/**
 * Exception thrown when a citizen cannot be found.
 */
public class CitizenNotFoundException extends RuntimeException {

    public CitizenNotFoundException(String message) {
        super(message);
    }
}
