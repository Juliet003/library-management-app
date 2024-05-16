package com.example.librarymanagementsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PatronNotFoundException extends RuntimeException {

    public PatronNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND;
    }

    private final HttpStatus status;
}
