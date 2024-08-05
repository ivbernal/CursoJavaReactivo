package com.example.tallerfinal.exceptions;

public class UserNoFoundException extends RuntimeException {
    public UserNoFoundException(String message) {
        super(message);
    }
}
