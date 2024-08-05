package com.example.tallerfinal.services.interfaces;


import com.example.tallerfinal.models.PaymentRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface IPaymentRestClient {

    @PostExchange("/payment")
    Mono<Boolean> validationPayment(@RequestBody String userId, @RequestBody double amount);

}
