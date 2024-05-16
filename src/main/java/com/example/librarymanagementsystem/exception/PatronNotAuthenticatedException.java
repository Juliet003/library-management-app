package com.example.librarymanagementsystem.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
public class PatronNotAuthenticatedException extends RuntimeException{
    private String message;
    private HttpStatus httpStatus;

    public PatronNotAuthenticatedException(String message) {
        this.message = message;
        this.httpStatus = HttpStatus.FORBIDDEN;
    }
}
