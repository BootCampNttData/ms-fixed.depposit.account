package com.bootcamp.fixeddeppossitaccount.service.Impl;

import com.bootcamp.fixeddeppossitaccount.model.FixedDepositAccountMovement;
import com.bootcamp.fixeddeppossitaccount.repository.FixedDepositAccountMovementRepository;
import com.bootcamp.fixeddeppossitaccount.service.FixedDepositAccountMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor

public class FixedDepositAccountMovementServiceImpl implements FixedDepositAccountMovementService {
    public final FixedDepositAccountMovementRepository repository;

    @Override
    public Mono<FixedDepositAccountMovement> create(FixedDepositAccountMovement fixedDepositAccountMovement) {
        return repository.save(fixedDepositAccountMovement);
    }

    @Override
    public Mono<FixedDepositAccountMovement> update(FixedDepositAccountMovement fixedDepositAccountMovement) {
        return repository.save(fixedDepositAccountMovement);
    }

    @Override
    public Mono deleteById(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono delete(FixedDepositAccountMovement fixedDepositAccountMovement) {
        return repository.delete(fixedDepositAccountMovement);
    }

    @Override
    public Mono<Double> getBalanceByAccount(Integer num) {
        var movements = this.findByAccountNumber(num);
        var balance = movements
                .map(mov -> {
                    return ("D".equals(mov.getMovementType()) ? 1 : -1 ) * mov.getAmount();
                }).reduce(0d, (a, b) -> a + b);
        return balance;
    }

    @Override
    public Flux<FixedDepositAccountMovement> findAll() {
        return repository.findAll();
    }

    @Override
    public Flux<FixedDepositAccountMovement> findByAccountNumber(Integer num) {
        return repository.findByAccountNumber(num);
    }
}