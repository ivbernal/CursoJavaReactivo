package com.example.tallerfinal.exceptions;

public class BadStatusException extends RuntimeException{
    public BadStatusException(String message) {
        super(message);
    }
}
