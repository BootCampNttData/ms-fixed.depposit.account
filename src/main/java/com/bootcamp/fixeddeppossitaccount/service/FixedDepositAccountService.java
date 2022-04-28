package com.bootcamp.fixeddeppossitaccount.service;

import com.bootcamp.fixeddeppossitaccount.model.FixedDepositAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FixedDepositAccountService {
    Flux<FixedDepositAccount> findAll();
    Mono<FixedDepositAccount> create(FixedDepositAccount fixedDepositAccount);
    Mono<FixedDepositAccount> findByAccountNumber(Integer number);
    Flux<FixedDepositAccount> findByClientId(String clientId);
    Mono<FixedDepositAccount> update(FixedDepositAccount fixedDepositAccount);
    Mono<FixedDepositAccount> deleteById(String id);
    Mono delete(FixedDepositAccount fixedDepositAccount);
}
