package com.bootcamp.fixeddeppossitaccount.repository;

import com.bootcamp.fixeddeppossitaccount.model.FixedDepositAccount;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FixedDepositAccountRepository extends ReactiveCrudRepository<FixedDepositAccount, String> {
    Mono<FixedDepositAccount> findByAccountNumber(Integer number);
    Flux<FixedDepositAccount> findByClientId(String clientId) ;
}