package com.example.tallerfinal.controllers;

import com.example.tallerfinal.models.AmountRequest;
import com.example.tallerfinal.models.User;
import com.example.tallerfinal.services.UserService;
import com.example.tallerfinal.services.interfaces.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(UserController.class)
class UserControllerTest {

/*    @Mock
    private UserService service;*/

    @MockBean
    private IUserService userService;

    @InjectMocks
    private UserController userController;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void crearUser_ShouldCreateUserAndReturn() {
        User user = new User();
        user.setId("userId");
        user.setName("John Doe");
        user.setBalance(10.0);

        when(userService.crear(any(User.class))).thenReturn(Mono.just(user));

        webTestClient.post()
                .uri("/users")
                .bodyValue(user)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(returnedUser -> assertThat(returnedUser)
                        .usingRecursiveComparison()
                        .isEqualTo(user));
    }

    @Test
    public void testActualizarUserBalance_Success() {
        User existingUser = new User();
        existingUser.setId("userId");
        existingUser.setName("John Doe");
        existingUser.setBalance(50.0);
        AmountRequest amountRequest = new AmountRequest();
        amountRequest.setAmount(25.0);

        when(userService.obtenerPorId(anyString())).thenReturn(Mono.just(existingUser));
        when(userService.crear(any(User.class))).thenReturn(Mono.just(existingUser));

        webTestClient.put()
                .uri("/users/{id}/balance", "userId")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(amountRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .consumeWith(response -> {
                    User user = response.getResponseBody();
                    assert user != null;
                    assert user.getBalance() == 75.0;
                });
    }

    @Test
    public void testObtenerUser_Success() {
        User user = new User();
        user.setId("userId");
        user.setName("John Doe");
        user.setBalance(50.0);

        when(userService.obtenerPorId(anyString())).thenReturn(Mono.just(user));

        webTestClient.get()
                .uri("/users/{id}", "userId")
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .consumeWith(response -> {
                    User responseBody = response.getResponseBody();
                    assert responseBody != null;
                    assert responseBody.getId().equals("userId");
                    assert responseBody.getName().equals("John Doe");
                    assert responseBody.getBalance() == 50.0;
                });
    }

    @Test
    public void testObteneTodosLosUser_Success() {
        User user1 = new User();
        user1.setId("userId1");
        user1.setName("John Doe");
        user1.setBalance(50.0);
        User user2 = new User();
        user2.setId("userId2");
        user2.setName("Jane Doe");
        user2.setBalance(75.0);

        when(userService.obtener()).thenReturn(Flux.just(user1, user2));

        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .consumeWith(response -> {
                    var users = response.getResponseBody();
                    assert users != null;
                    assert users.size() == 2;
                    assert users.get(0).getId().equals("userId1");
                    assert users.get(0).getName().equals("John Doe");
                    assert users.get(0).getBalance() == 50.0;
                    assert users.get(1).getId().equals("userId2");
                    assert users.get(1).getName().equals("Jane Doe");
                    assert users.get(1).getBalance() == 75.0;
                });
    }

}