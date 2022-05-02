package com.bootcamp.fixeddeppossitaccount.repository;

import com.bootcamp.fixeddeppossitaccount.model.FixedDepositAccountMovement;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface FixedDepositAccountMovementRepository extends ReactiveCrudRepository<FixedDepositAccountMovement, String> {
    Flux<FixedDepositAccountMovement> findByAccountNumber(String id);
}