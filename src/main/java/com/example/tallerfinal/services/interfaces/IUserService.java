package com.example.tallerfinal.services.interfaces;

import com.example.tallerfinal.models.AmountRequest;
import com.example.tallerfinal.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserService {

    Mono<User> crear(User user);
    Flux<User> obtener();
    Mono<User> obtenerPorId(String id);

    Mono<User> actualizarUserBalance(String id, Double amount);



}


