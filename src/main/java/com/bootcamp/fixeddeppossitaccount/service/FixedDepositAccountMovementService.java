package com.bootcamp.fixeddeppossitaccount.service;

import com.bootcamp.fixeddeppossitaccount.model.FixedDepositAccountMovement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FixedDepositAccountMovementService {
    Flux<FixedDepositAccountMovement> findAll();
    Mono<FixedDepositAccountMovement> create(FixedDepositAccountMovement fixedDepositAccountMovement);
    Flux<FixedDepositAccountMovement> findByAccountNumber(Integer num);
    Mono<FixedDepositAccountMovement> update(FixedDepositAccountMovement fixedDepositAccountMovement);
    Mono<FixedDepositAccountMovement> deleteById(String id);
    Mono delete(FixedDepositAccountMovement fixedDepositAccountMovement);
    Mono<Double> getBalanceByAccount(Integer num);
}
