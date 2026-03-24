package se.trollhattan.citizenapi.exception;

public class CitizenNotFoundException extends RuntimeException {

    public CitizenNotFoundException(String message) {
        super(message);
    }
}
