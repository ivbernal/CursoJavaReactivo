package com.example.tallerfinal.repositories;

import com.example.tallerfinal.models.Cashout;
import com.example.tallerfinal.models.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CashoutRepository extends ReactiveMongoRepository<Cashout, String> {
    Flux<Cashout> findByUserId(String userID);
}
