package com.example.tallerfinal.controllers;

import com.example.tallerfinal.models.Cashout;
import com.example.tallerfinal.services.interfaces.ICashoutService;
import com.example.tallerfinal.services.interfaces.ITransactionHistoryRestClient;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cashouts")
public class CashoutController {
    private final ICashoutService service;
    private final ITransactionHistoryRestClient transactionHistoryRestClient;

    public CashoutController(ICashoutService service, ITransactionHistoryRestClient transactionHistoryRestClient) {
        this.service = service;
        this.transactionHistoryRestClient = transactionHistoryRestClient;
    }

    @PostMapping
    public Mono<Cashout> crearCashOut(@Valid @RequestBody Cashout cashout){

        return service.crear(cashout);
    }

    @GetMapping("/user/{userId}")
    public Flux<Cashout> obtenerPorUserId(@PathVariable String userId){

        return transactionHistoryRestClient.cashOutList(userId);
    }
}
