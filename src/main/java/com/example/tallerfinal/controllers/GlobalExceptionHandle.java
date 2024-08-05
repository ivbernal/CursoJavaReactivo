package com.example.tallerfinal.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import com.example.tallerfinal.exceptions.BadStatusException;
import com.example.tallerfinal.exceptions.UserNoFoundException;
import com.example.tallerfinal.exceptions.NotEnoughtBalanceException;
import com.example.tallerfinal.exceptions.Exception400;

@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(Exception400.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<String>> handleException400Exception(Exception400 exception){
        return Mono.just(ResponseEntity.badRequest().body(exception.getMessage()));
    }

    @ExceptionHandler(BadStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<String>> handleBadStatusException(BadStatusException exception){
        return Mono.just(ResponseEntity.badRequest().body(exception.getMessage()));
    }

    @ExceptionHandler(NotEnoughtBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<String>> handleNotEnoughtBalanceException(NotEnoughtBalanceException exception){
        return Mono.just(ResponseEntity.badRequest().body(exception.getMessage()));
    }

    @ExceptionHandler(UserNoFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<String>> handleUserNoFoundException(UserNoFoundException exception){
        return Mono.just(ResponseEntity.badRequest().body(exception.getMessage()));
    }


}
