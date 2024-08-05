package com.example.tallerfinal.controllers;


import com.example.tallerfinal.models.Cashout;
import com.example.tallerfinal.services.interfaces.ICashoutService;
import com.example.tallerfinal.services.interfaces.ITransactionHistoryRestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CashoutController.class})
@WebFluxTest(CashoutController.class)
class CashoutControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    ICashoutService service;

    @MockBean
    ITransactionHistoryRestClient transactionHistoryRestClient;

    @Test
    void crearCashOut() {
        var cashout = new Cashout();
        cashout.setAmount(1000.0);
        cashout.setUserId("user123");

        when(service.crear(refEq(cashout))).thenReturn(Mono.just(cashout));

        webTestClient.post()
                .uri("/cashouts")
                .bodyValue(cashout)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.amount").isEqualTo(1000.0)
                .jsonPath("$.userId").isEqualTo("user123");
    }

    @Test
    void obtenerPorUserId() {
        var cashout1 = new Cashout();
        cashout1.setAmount(1000.0);
        cashout1.setUserId("user123");

        var cashout2 = new Cashout();
        cashout2.setAmount(2000.0);
        cashout2.setUserId("user123");

        when(transactionHistoryRestClient.cashOutList("user123")).thenReturn(Flux.just(cashout1, cashout2));

        webTestClient.get()
                .uri("/cashouts/user/{userId}", "user123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].amount").isEqualTo(1000.0)
                .jsonPath("$[0].userId").isEqualTo("user123")
                .jsonPath("$[1].amount").isEqualTo(2000.0)
                .jsonPath("$[1].userId").isEqualTo("user123");
    }
}