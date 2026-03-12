package se.trollhattan.citizenapi.exception;

import java.time.LocalDateTime;

/**
 * Standard error response body returned by the API.
 *
 * @param status    the HTTP status code
 * @param error     a short error label
 * @param message   a description of what went wrong
 * @param timestamp when the error occurred
 */
public record ApiError(
        int status,
        String error,
        String message,
        LocalDateTime timestamp) {

    /**
     * Creates an ApiError with the current timestamp.
     *
     * @param status  HTTP status code
     * @param error   short error label
     * @param message descriptive error message
     * @return a new ApiError with the current timestamp
     */
    public static ApiError of(int status, String error, String message) {
        return new ApiError(status, error, message, LocalDateTime.now());
    }
}
