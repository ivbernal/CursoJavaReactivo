package com.example.tallerfinal.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = AuxiliarPaymentController.class)
public class AuxiliarPaymentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        Mockito.reset();
    }

    @Test
    void validationPayment_Success() {
        double amount = 500.0;
        String userId = "user123";

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/payment")
                        .queryParam("userId", userId)
                        .build())
                .bodyValue(amount)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class).isEqualTo(true);
    }
}
