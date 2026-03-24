package se.trollhattan.citizenapi.exception;

public class NavetUnavailableException extends RuntimeException {

    public NavetUnavailableException(String message) {
        super(message);
    }

    public NavetUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
