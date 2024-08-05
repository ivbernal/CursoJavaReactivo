package com.example.tallerfinal.services;

import com.example.tallerfinal.exceptions.BadStatusException;
import com.example.tallerfinal.exceptions.NotEnoughtBalanceException;
import com.example.tallerfinal.exceptions.UserNoFoundException;
import com.example.tallerfinal.models.Cashout;
import com.example.tallerfinal.models.PaymentRequest;
import com.example.tallerfinal.models.User;
import com.example.tallerfinal.repositories.CashoutRepository;
import com.example.tallerfinal.services.interfaces.ICashoutService;
import com.example.tallerfinal.services.interfaces.IPaymentRestClient;
import com.example.tallerfinal.services.interfaces.ITransactionHistoryRestClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
public class CashoutService implements ICashoutService {

    private final CashoutRepository cashoutRepository;
    private final UserService userService;
    private final IPaymentRestClient paymentRestClient;

    public CashoutService(CashoutRepository cashoutRepository, UserService userService, IPaymentRestClient paymentRestClient) {
        this.cashoutRepository = cashoutRepository;
        this.userService = userService;
        this.paymentRestClient = paymentRestClient;
    }

    @Override
    public Mono<Cashout> crear(Cashout cashout) {
        return userService.obtenerPorId(cashout.getUserId())
                .switchIfEmpty(Mono.error(new UserNoFoundException("Usuario no existe")))
                .flatMap(user -> validarBalance(user, cashout.getAmount())
                        .flatMap(validUser -> paymentRestClient.validationPayment(cashout.getUserId(), cashout.getAmount())
                                .flatMap(isValid -> {
                                    if (!isValid) {
                                        return Mono.error(new BadStatusException("Solicitud rechazada"));
                                    }
                                    return actualizarBalanceYGuardarCashout(validUser, cashout);
                                })
                        )
                );
    }

    private Mono<User> validarBalance(User user, double amount) {
        if (user.getBalance() < amount) {
            return Mono.error(new NotEnoughtBalanceException("Saldo insuficiente"));
        }
        user.setBalance(user.getBalance() - amount);
        return Mono.just(user);
    }

    private Mono<Cashout> actualizarBalanceYGuardarCashout(User user, Cashout cashout) {
        return userService.actualizarUserBalance(user.getId(), user.getBalance())
                .then(cashoutRepository.save(cashout));
    }

    /*
    @Override
    public Mono<Cashout> crear(Cashout cashout) {
        return userService.obtenerPorId(cashout.getUserId())
                .switchIfEmpty(Mono.error(new UserNoFoundException("Usuario no éxiste")))
                .flatMap(user -> {
                    if (user.getBalance() < cashout.getAmount()){
                        return Mono.error(new NotEnoughtBalanceException("Saldo insufieciente"));
                    }
                    user.setBalance(user.getBalance()-cashout.getAmount());
                    return paymentRestClient.paymentStatus(cashout.getUserId(), cashout.getAmount())
                            .flatMap(valid -> {
                                if (!valid){
                                    return Mono.error(new BadStatusException("Solicitud rechazada"));
                                }

                                return userService.actualizarUserBalance(user.getId(), user.getBalance())
                                        .flatMap(updateUser -> cashoutRepository.save(cashout));
                            });

                });

    }

     */

    @Override
    public Flux<Cashout> obtener() {

        return cashoutRepository.findAll();
    }

    @Override
    public Flux<Cashout> obtenerPorUserId(String id) {


        return cashoutRepository.findByUserId(id)
                .switchIfEmpty(Mono.error(new RuntimeException("No existen cashouts para este id")));
    }
}
