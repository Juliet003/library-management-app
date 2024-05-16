package com.example.librarymanagementsystem.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
public class BookNotAvailableException extends RuntimeException{
    private String message;
    private HttpStatus httpStatus;

    public BookNotAvailableException(String message) {
        super(message);
        this.message = message;
        this.httpStatus = HttpStatus.CONFLICT;
    }
}
