package com.example.tallerfinal.services;

import com.example.tallerfinal.exceptions.UserNoFoundException;
import com.example.tallerfinal.models.AmountRequest;
import com.example.tallerfinal.models.User;
import com.example.tallerfinal.repositories.UserRepository;
import com.example.tallerfinal.services.interfaces.IUserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Mono<User> crear(User user) {
        return userRepository.save(user);
    }

    @Override
    public Flux<User> obtener() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> obtenerPorId(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNoFoundException("Cliente no encotrado")));
    }

    @Override
    public Mono<User> actualizarUserBalance(String id, Double amount) {
        return  userRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encotrado")))
                .flatMap( existUser-> {
                    existUser.setBalance(amount);
                    return userRepository.save(existUser);
                });
    }
}
