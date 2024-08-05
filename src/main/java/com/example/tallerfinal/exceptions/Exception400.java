package com.example.tallerfinal.exceptions;

public class Exception400 extends RuntimeException{
    public Exception400(String errorBody) {
        super(errorBody);
    }
}
