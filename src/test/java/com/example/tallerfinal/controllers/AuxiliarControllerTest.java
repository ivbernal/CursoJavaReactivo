package com.example.tallerfinal.controllers;

import com.example.tallerfinal.controllers.AuxiliarController;
import com.example.tallerfinal.models.Cashout;
import com.example.tallerfinal.services.interfaces.ICashoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = AuxiliarController.class)
public class AuxiliarControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ICashoutService cashoutService;

    @BeforeEach
    void setUp() {
        Mockito.reset(cashoutService);
    }

    @Test
    void obtenerPorUserId() {
        Cashout cashout1 = new Cashout();
        cashout1.setId("cashout1");
        cashout1.setUserId("user123");
        cashout1.setAmount(100.0);

        Cashout cashout2 = new Cashout();
        cashout2.setId("cashout2");
        cashout2.setUserId("user123");
        cashout2.setAmount(200.0);

        when(cashoutService.obtenerPorUserId(anyString())).thenReturn(Flux.just(cashout1, cashout2));

        webTestClient.get()
                .uri("/transaction-history/user/{userId}", "user123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("cashout1")
                .jsonPath("$[0].userId").isEqualTo("user123")
                .jsonPath("$[0].amount").isEqualTo(100.0)
                .jsonPath("$[1].id").isEqualTo("cashout2")
                .jsonPath("$[1].userId").isEqualTo("user123")
                .jsonPath("$[1].amount").isEqualTo(200.0);
    }

    @Test
    void obtenerPorUserId_UserNotFound() {
        when(cashoutService.obtenerPorUserId(anyString())).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/transaction-history/user/{userId}", "user456")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(0);
    }
}
