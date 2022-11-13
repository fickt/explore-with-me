package ru.yandex.project.service.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Handler {

    private static final String REASON_NOT_FOUND = "The required object was not found.";
    private static final String REASON_INTEGRITY_VIOLATION = "Integrity constraint has been violated";
    private static final String REASON_CONDITIONS_NOT_MET = "For the requested operation the conditions are not met.";

    @ExceptionHandler
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException e) {
        var response = new ApiError();
        response.setStatus(HttpStatus.NOT_FOUND.getReasonPhrase());
        response.setReason(REASON_NOT_FOUND);
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ApiError> handleConstraintViolationException(Exception e) {
        var response = new ApiError();
        response.setStatus(HttpStatus.CONFLICT.getReasonPhrase());
        response.setReason(REASON_INTEGRITY_VIOLATION);
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleNotEventInitiatorException(NotEventInitiatorException e) {
        var response = new ApiError();
        response.setStatus(HttpStatus.FORBIDDEN.getReasonPhrase());
        response.setReason(REASON_CONDITIONS_NOT_MET);
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleNotRequesterException(NotRequesterException e) {
        var response = new ApiError();
        response.setStatus(HttpStatus.FORBIDDEN.getReasonPhrase());
        response.setReason(REASON_CONDITIONS_NOT_MET);
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleWrongEventStatusToChangeException(WrongEventStatusToChangeException e) {
        var response = new ApiError();
        response.setStatus(HttpStatus.FORBIDDEN.getReasonPhrase());
        response.setReason(REASON_CONDITIONS_NOT_MET);
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleExceedParticipantLimitException(ExceedParticipantLimitException e) {
        var response = new ApiError();
        response.setStatus(HttpStatus.FORBIDDEN.getReasonPhrase());
        response.setReason(REASON_CONDITIONS_NOT_MET);
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleUnavailableEventException(UnavailableEventException e) {
        var response = new ApiError();
        response.setStatus(HttpStatus.FORBIDDEN.getReasonPhrase());
        response.setReason(REASON_CONDITIONS_NOT_MET);
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
