package com.example.librarymanagementsystem.exception;


import com.example.librarymanagementsystem.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(NOT_ACCEPTABLE)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        });
        return new ResponseEntity<>(new ApiResponse<>("Validation Failed", "success", NOT_ACCEPTABLE,  errors), NOT_ACCEPTABLE);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(NOT_ACCEPTABLE)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getAllErrors().forEach(violation -> {
            String propertyPath = violation.getCode();
            String message = violation.getDefaultMessage();
            errors.put(propertyPath, message);
        });
        return new ResponseEntity<>(new ApiResponse<>("Validation Failed", "success", NOT_ACCEPTABLE,  errors), NOT_ACCEPTABLE);
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ApiResponse<String>> handleProductNotFoundException(BookNotFoundException e) {
        return new ResponseEntity<>(new ApiResponse<>(NOT_FOUND, e.getLocalizedMessage()), NOT_FOUND );
    }

    @ExceptionHandler(BookAlreadyExistException.class)
    @ResponseStatus(CONFLICT)
    public ResponseEntity<ApiResponse<String>> handleProductAlreadyExistException(BookAlreadyExistException e) {
        return new ResponseEntity<>(new ApiResponse<>(CONFLICT, e.getLocalizedMessage()), CONFLICT );
    }

    @ExceptionHandler(PatronExistException.class)
    @ResponseStatus(CONFLICT)
    public ResponseEntity<ApiResponse<String>> handlePatronExistException(PatronExistException e) {
        return new ResponseEntity<>(new ApiResponse<>(CONFLICT, e.getLocalizedMessage()), CONFLICT );
    }

    @ExceptionHandler(PatronNotAuthenticatedException.class)
    @ResponseStatus(FORBIDDEN)
    public ResponseEntity<ApiResponse<String>> handlePatronNotAuthenticatedException(PatronNotAuthenticatedException e) {
        return new ResponseEntity<>(new ApiResponse<>(FORBIDDEN, e.getLocalizedMessage()), FORBIDDEN );
    }

    @ExceptionHandler(NotAuthorizedException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ResponseEntity<ApiResponse<String>> handleBookOperationException(NotAuthorizedException e) {
        return new ResponseEntity<>(new ApiResponse<>(UNAUTHORIZED, e.getLocalizedMessage()), UNAUTHORIZED );
    }

    @ExceptionHandler(PatronNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ApiResponse<String>> handlePatronNotFoundException(PatronNotFoundException e) {
        return new ResponseEntity<>(new ApiResponse<>(NOT_FOUND, e.getLocalizedMessage()), NOT_FOUND );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ApiResponse<>(BAD_REQUEST, e.getLocalizedMessage()), BAD_REQUEST );
    }

    @ExceptionHandler(InvalidCredentialException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleInvalidCredentialException(InvalidCredentialException e) {
        return new ResponseEntity<>(new ApiResponse<>(BAD_REQUEST, e.getLocalizedMessage()), BAD_REQUEST );
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleIllegalStateException(IllegalStateException e) {
        return new ResponseEntity<>(new ApiResponse<>(BAD_REQUEST, e.getLocalizedMessage()), BAD_REQUEST );
    }

    @ExceptionHandler(JwtParsingException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleJwtParsingException(JwtParsingException e) {
        return new ResponseEntity<>(new ApiResponse<>(BAD_REQUEST, e.getLocalizedMessage()), BAD_REQUEST);
    }
}
