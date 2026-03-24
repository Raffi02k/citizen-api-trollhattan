package se.trollhattan.citizenapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiError errorResponse = new ApiError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(CitizenNotFoundException.class)
    public ResponseEntity<ApiError> handleCitizenNotFoundException(CitizenNotFoundException ex) {
        ApiError errorResponse = new ApiError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(NavetBadRequestException.class)
    public ResponseEntity<ApiError> handleNavetBadRequestException(NavetBadRequestException ex) {
        ApiError errorResponse = new ApiError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse);
    }

    @ExceptionHandler(NavetUnauthorizedException.class)
    public ResponseEntity<ApiError> handleNavetUnauthorizedException(NavetUnauthorizedException ex) {
        ApiError errorResponse = new ApiError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse);
    }

    @ExceptionHandler(NavetRateLimitException.class)
    public ResponseEntity<ApiError> handleNavetRateLimitException(NavetRateLimitException ex) {
        ApiError errorResponse = new ApiError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
    }

    @ExceptionHandler(NavetUnavailableException.class)
    public ResponseEntity<ApiError> handleNavetUnavailableException(NavetUnavailableException ex) {
        ApiError errorResponse = new ApiError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }
}