package com.bootcamp.fixeddeppossitaccount.service.Impl;

import com.bootcamp.fixeddeppossitaccount.model.FixedDepositAccount;
import com.bootcamp.fixeddeppossitaccount.repository.FixedDepositAccountRepository;
import com.bootcamp.fixeddeppossitaccount.service.FixedDepositAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor

public class FixedDepositAccountServiceImpl implements FixedDepositAccountService {
    public final FixedDepositAccountRepository repository;

    @Override
    public Mono<FixedDepositAccount> create(FixedDepositAccount fixedDepositAccount) {
        return repository.save(fixedDepositAccount);
    }

    @Override
    public Mono<FixedDepositAccount> update(FixedDepositAccount fixedDepositAccount) {
        return repository.save(fixedDepositAccount);
    }

    @Override
    public Mono deleteById(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono delete(FixedDepositAccount fixedDepositAccount) {
        return repository.delete(fixedDepositAccount);
    }

    @Override
    public Flux<FixedDepositAccount> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<FixedDepositAccount> findByAccountNumber(Integer number) {
        return repository.findByAccountNumber(number);
    }
    @Override
    public Flux<FixedDepositAccount> findByClientId(String clientId) {
        return repository.findByClientId(clientId);
    }

}