package com.example.librarymanagementsystem.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
public class PatronExistException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;

    public PatronExistException(String message) {
        this.message = message;
        this.httpStatus = HttpStatus.CONFLICT;
    }
}
