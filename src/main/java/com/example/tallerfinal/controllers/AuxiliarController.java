package com.example.tallerfinal.controllers;

import com.example.tallerfinal.models.Cashout;
import com.example.tallerfinal.services.interfaces.ICashoutService;
import com.example.tallerfinal.services.interfaces.IPaymentRestClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/transaction-history/user/{userId}")
public class AuxiliarController {

    private final ICashoutService service;

    public AuxiliarController(ICashoutService service) {
        this.service = service;
    }

    @GetMapping()
    public Flux<Cashout> obtenerPorUserId(@PathVariable String userId){
        return service.obtenerPorUserId(userId);
    }
}
