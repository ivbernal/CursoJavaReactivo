package com.example.tallerfinal.services;

import com.example.tallerfinal.exceptions.BadStatusException;
import com.example.tallerfinal.exceptions.UserNoFoundException;
import com.example.tallerfinal.exceptions.NotEnoughtBalanceException;
import com.example.tallerfinal.models.Cashout;
import com.example.tallerfinal.models.User;
import com.example.tallerfinal.repositories.CashoutRepository;
import com.example.tallerfinal.services.interfaces.IPaymentRestClient;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class CashoutServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private IPaymentRestClient paymentRestClient;

    @Mock
    private CashoutRepository cashoutRepository;

    @InjectMocks
    private CashoutService cashoutService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testCrear_Success() {
        Cashout cashout = new Cashout();
        cashout.setUserId("userId");
        cashout.setAmount(100.0);

        User user = new User();
        user.setId("userId");
        user.setBalance(200.0);

        when(userService.obtenerPorId(anyString())).thenReturn(Mono.just(user));
        when(paymentRestClient.validationPayment(anyString(), anyDouble())).thenReturn(Mono.just(true));
        when(cashoutRepository.save(any(Cashout.class))).thenReturn(Mono.just(cashout));
        when(userService.actualizarUserBalance(anyString(), anyDouble())).thenReturn(Mono.just(user));

        Mono<Cashout> result = cashoutService.crear(cashout);

        StepVerifier.create(result)
                .expectNext(cashout)
                .verifyComplete();
    }

    @Test
    public void crear_ShouldThrowUserNoFoundExceptionWhenUserNotFound() {

        Cashout cashout = new Cashout();
        cashout.setUserId("userId");
        cashout.setAmount(50.0);

        when(userService.obtenerPorId(anyString())).thenReturn(Mono.empty());

        Mono<Cashout> result = cashoutService.crear(cashout);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof UserNoFoundException && throwable.getMessage().equals("Usuario no existe"))
                .verify();
    }

    @Test
    public void obtener_ShouldReturnCashouts() {

        Cashout cashout1 = new Cashout();
        cashout1.setId("1");
        cashout1.setAmount(100.0);

        Cashout cashout2 = new Cashout();
        cashout2.setId("2");
        cashout2.setAmount(200.0);

        when(cashoutRepository.findAll()).thenReturn(Flux.fromIterable(Arrays.asList(cashout1, cashout2)));

        Flux<Cashout> result = cashoutService.obtener();

        StepVerifier.create(result)
                .expectNext(cashout1)
                .expectNext(cashout2)
                .verifyComplete();
    }

    @Test
    public void obtenerPorUserId_ShouldReturnCashouts() {

        String userId = "userId";
        Cashout cashout1 = new Cashout();
        cashout1.setId("1");
        cashout1.setUserId(userId);
        cashout1.setAmount(100.0);

        Cashout cashout2 = new Cashout();
        cashout2.setId("2");
        cashout2.setUserId(userId);
        cashout2.setAmount(200.0);

        when(cashoutRepository.findByUserId(anyString())).thenReturn(Flux.fromIterable(Arrays.asList(cashout1, cashout2)));

        Flux<Cashout> result = cashoutService.obtenerPorUserId(userId);

        StepVerifier.create(result)
                .expectNext(cashout1)
                .expectNext(cashout2)
                .verifyComplete();
    }

    @Test
    public void testCrear_ValidationPaymentFailed() {

        Cashout cashout = new Cashout();
        cashout.setUserId("userId");
        cashout.setAmount(100.0);

        User user = new User();
        user.setId("userId");
        user.setBalance(200.0);

        when(userService.obtenerPorId(anyString())).thenReturn(Mono.just(user));
        when(paymentRestClient.validationPayment(anyString(), anyDouble())).thenReturn(Mono.just(false));

        Mono<Cashout> result = cashoutService.crear(cashout);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof BadStatusException && throwable.getMessage().equals("Solicitud rechazada"))
                .verify();
    }

    @Test
    public void testCrear_InsufficientBalance() {

        Cashout cashout = new Cashout();
        cashout.setUserId("userId");
        cashout.setAmount(100.0);

        User user = new User();
        user.setId("userId");
        user.setBalance(200.0);
        user.setBalance(50.0);

        when(userService.obtenerPorId(anyString())).thenReturn(Mono.just(user));

        Mono<Cashout> result = cashoutService.crear(cashout);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof NotEnoughtBalanceException && throwable.getMessage().equals("Saldo insuficiente"))
                .verify();
    }

}