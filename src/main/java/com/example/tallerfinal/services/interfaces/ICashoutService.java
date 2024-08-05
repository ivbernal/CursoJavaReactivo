package com.example.tallerfinal.services.interfaces;

import com.example.tallerfinal.models.Cashout;
import com.example.tallerfinal.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICashoutService {
    Mono<Cashout> crear(Cashout cashout);
    Flux<Cashout> obtener();
    Flux<Cashout> obtenerPorUserId(String id);
}
