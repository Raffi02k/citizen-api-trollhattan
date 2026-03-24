package se.trollhattan.citizenapi.exception;

public class NavetUnauthorizedException extends RuntimeException {

    public NavetUnauthorizedException(String message) {
        super(message);
    }

    public NavetUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
