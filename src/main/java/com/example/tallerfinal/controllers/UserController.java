package com.example.tallerfinal.controllers;

import com.example.tallerfinal.exceptions.UserNoFoundException;
import com.example.tallerfinal.repositories.UserRepository;
import com.example.tallerfinal.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.tallerfinal.models.User;
import com.example.tallerfinal.models.AmountRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

  private final IUserService service;

    public UserController(IUserService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<User> crearUsere(@Valid @RequestBody User user){

        return service.crear(user);
    }

    @GetMapping
    public Flux<User> obteneTodosLosUser(){

        return service.obtener();
    }

    @GetMapping("/{id}")
    public Mono<User> obtenerUser(@PathVariable String id){

        return service.obtenerPorId(id);
    }

    @PutMapping("/{id}/balance")
    public Mono<User> actualizarUserBalance(@PathVariable String id,@RequestBody AmountRequest amount){
        return service.obtenerPorId(id)
                .switchIfEmpty(Mono.error(new UserNoFoundException("Usuario no encontrado")))
                .flatMap(existUser ->{
                    existUser.setBalance(existUser.getBalance() + amount.getAmount());
                    return service.crear(existUser);
                });//return service.actualizarUserBalance(id, amountRequest.getAmount());
    }
}
