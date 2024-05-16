package com.example.librarymanagementsystem.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
public class NotAuthorizedException extends RuntimeException{
    private String message;
    private HttpStatus httpStatus;

    public NotAuthorizedException(String message) {
        this.message = message;
        this.httpStatus = HttpStatus.UNAUTHORIZED;
    }
}
