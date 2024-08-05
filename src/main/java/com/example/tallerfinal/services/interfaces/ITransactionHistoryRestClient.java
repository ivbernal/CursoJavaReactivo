package com.example.tallerfinal.services.interfaces;

import com.example.tallerfinal.models.Cashout;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Flux;

public interface ITransactionHistoryRestClient {

    @GetExchange("/transaction-history/user/{userId}")
    Flux<Cashout> cashOutList(@PathVariable("userId") String userId);

}
