package com.example.librarymanagementsystem.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
public class BookNotFoundException extends RuntimeException{
    private String message;
    private HttpStatus httpStatus;

    public BookNotFoundException(String message) {
        this.message = message;
        this.httpStatus = HttpStatus.NOT_FOUND;
    }
}
