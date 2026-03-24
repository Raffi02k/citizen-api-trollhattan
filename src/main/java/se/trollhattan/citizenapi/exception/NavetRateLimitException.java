package se.trollhattan.citizenapi.exception;

public class NavetRateLimitException extends RuntimeException {

    public NavetRateLimitException(String message) {
        super(message);
    }

    public NavetRateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
