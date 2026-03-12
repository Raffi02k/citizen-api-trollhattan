package se.trollhattan.citizenapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler that returns standardized ApiError responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Returns a 404 response when a citizen is not found.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing an ApiError body and 404 status
     */
    @ExceptionHandler(CitizenNotFoundException.class)
    public ResponseEntity<ApiError> handleCitizenNotFound(CitizenNotFoundException ex) {
        // TODO Phase 2: Add logging here, e.g., log.warn("Citizen not found: {}",
        // ex.getMessage());

        ApiError error = ApiError.of(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Returns a 500 response for unexpected exceptions.
     *
     * @param ex the unexpected exception
     * @return a ResponseEntity containing an ApiError body and 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        // TODO Phase 2: Add logging here, e.g., log.error("Unexpected error", ex);

        ApiError error = ApiError.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
