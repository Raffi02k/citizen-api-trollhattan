package se.trollhattan.citizenapi.exception;

/**
 * Exception thrown when a citizen cannot be found.
 */
public class CitizenNotFoundException extends RuntimeException {

    /**
     * Creates a new exception with a descriptive message.
     *
     * @param message a description of what was not found
     */
    public CitizenNotFoundException(String message) {
        super(message);
    }
}
