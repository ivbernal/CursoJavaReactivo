package com.example.tallerfinal.exceptions;

public class NotEnoughtBalanceException extends RuntimeException {
    public NotEnoughtBalanceException(String message) {
        super(message);
    }
}
