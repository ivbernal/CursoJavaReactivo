package com.example.tallerfinal.services;

import com.example.tallerfinal.exceptions.UserNoFoundException;
import com.example.tallerfinal.models.User;
import com.example.tallerfinal.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void crear_ShouldSaveUser() {
        // Arrange
        User user = new User();
        user.setId("123");
        user.setName("John Doe");

        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));

        // Act
        Mono<User> result = userService.crear(user);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(savedUser -> savedUser.getId().equals("123") && savedUser.getName().equals("John Doe"))
                .verifyComplete();
    }

    @Test
    public void obtener_ShouldReturnUsers() {

        User user1 = new User();
        user1.setId("1");
        user1.setName("John Doe");

        User user2 = new User();
        user2.setId("2");
        user2.setName("Jane Doe");

        when(userRepository.findAll()).thenReturn(Flux.fromIterable(Arrays.asList(user1, user2)));

        Flux<User> result = userService.obtener();

        StepVerifier.create(result)
                .expectNext(user1)
                .expectNext(user2)
                .verifyComplete();
    }

    @Test
    public void obtener_ShouldReturnEmpty_WhenNoUsersFound() {
        when(userRepository.findAll()).thenReturn(Flux.empty());

        Flux<User> result = userService.obtener();

        StepVerifier.create(result)
                .verifyComplete();
    }


    @Test
    public void obtenerPorId_UserFound_ShouldReturnUser() {

        String userId = "123";
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("John Doe");

        when(userRepository.findById(anyString())).thenReturn(Mono.just(existingUser));

        Mono<User> result = userService.obtenerPorId(userId);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getId().equals(userId) && user.getName().equals("John Doe"))
                .verifyComplete();
    }

    @Test
    public void obtenerPorId_UserNotFound_ShouldThrowException() {
        String userId = "123";

        when(userRepository.findById(anyString())).thenReturn(Mono.empty());

        Mono<User> result = userService.obtenerPorId(userId);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof UserNoFoundException &&
                        throwable.getMessage().equals("Cliente no encotrado"))
                .verify();
    }


    @Test
    public void actualizarUserBalance_UserFound_ShouldUpdateBalance() {
        String userId = "123";
        Double newBalance = 100.0;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setBalance(50.0);

        when(userRepository.findById(anyString())).thenReturn(Mono.just(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(existingUser));

        Mono<User> result = userService.actualizarUserBalance(userId, newBalance);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getBalance().equals(newBalance))
                .verifyComplete();
    }
}