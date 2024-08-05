package com.example.tallerfinal.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/payment")
public class AuxiliarPaymentController {

    @PostMapping()
    public Mono<Boolean> validationPayment(@RequestBody double amount, String userId){
        return Mono.just(true);
    }
}
