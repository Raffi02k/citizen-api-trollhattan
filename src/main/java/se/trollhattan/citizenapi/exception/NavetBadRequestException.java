package se.trollhattan.citizenapi.exception;

public class NavetBadRequestException extends RuntimeException {

    public NavetBadRequestException(String message) {
        super(message);
    }

    public NavetBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
