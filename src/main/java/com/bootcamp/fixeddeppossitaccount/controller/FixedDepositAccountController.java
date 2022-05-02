package com.bootcamp.fixeddeppossitaccount.controller;

import com.bootcamp.fixeddeppossitaccount.model.FixedDepositAccount;
import com.bootcamp.fixeddeppossitaccount.model.FixedDepositAccountMovement;
import com.bootcamp.fixeddeppossitaccount.service.FixedDepositAccountMovementService;
import com.bootcamp.fixeddeppossitaccount.service.FixedDepositAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/fixeddepositaccount")
@RequiredArgsConstructor
public class FixedDepositAccountController {
    public final FixedDepositAccountService service;
    public final FixedDepositAccountMovementService fixedDepositAccountMovementService;

    @GetMapping
    public Flux<FixedDepositAccount> getAll(){
        return service.findAll();
    }

    @GetMapping("/find/{num}")
    public Mono<FixedDepositAccount> findByAccountNumber(@PathVariable("num") Integer num){
        return service.findByAccountNumber(num);
    }

    /**
     * Obtiene una lista de las cuentas a Plazo Fijo que posea el Cliente segun su Documento
     * @param clientId Documento del Cliente (RUC)
     * @return Lista con las cuentas pertenecientes al Documento
     */
    @GetMapping("/findAcountsByClientId/{clientId}")
    public Flux<String> findAcountsByClientId(@PathVariable("clientId") String clientId) {
        var accounts = service.findByClientId(clientId);
        var lst = accounts.map(acc -> {
            return acc.getAccountNumber();
        });
        return lst;
    }

    /** Deposito a Cuenta Plazo Fijo
     *
     * @param fixedDepositAccount
     * @return
     */
    @PostMapping
    public Mono<FixedDepositAccount> create(@RequestBody FixedDepositAccount fixedDepositAccount){
        return service.create(fixedDepositAccount);
    }

    @PostMapping("/update")
    public Mono<FixedDepositAccount> update(@RequestBody FixedDepositAccount fixedDepositAccount){
        return service.create(fixedDepositAccount);
    }

    @DeleteMapping
    public Mono<FixedDepositAccount> delete(@RequestBody FixedDepositAccount fixedDepositAccount){
        return service.delete(fixedDepositAccount);
    }

    @DeleteMapping("/byId/{id}")
    public Mono<FixedDepositAccount> deleteById(@RequestBody String id){
        return service.deleteById(id);
    }

/** ******************************************************************************************************/

    @GetMapping("/movement")
    public Flux<FixedDepositAccountMovement> getAllMovements(){
        return fixedDepositAccountMovementService.findAll();
    }

    @GetMapping("movement/find/{num}")
    public Flux<FixedDepositAccountMovement> getByAccountNumber(@PathVariable("num") String num){
        return fixedDepositAccountMovementService.findByAccountNumber(num);
    }

    /**
     * Realiza deposito o retiro de cuenta a plazo fijo
     * @param fixedDepositAccountMovement
     * @return
     */
    @PostMapping("/movement")
    public Mono<FixedDepositAccountMovement> create(@RequestBody FixedDepositAccountMovement fixedDepositAccountMovement) {
        SimpleDateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy");

        Date mvDate=fixedDepositAccountMovement.getMovementDate();
        String mvType=fixedDepositAccountMovement.getMovementType();
        var account = service.findByAccountNumber(fixedDepositAccountMovement.getAccountNumber());
        var _ok = account.flatMap(acc -> {
            try {
                var withdrawalDate = acc.getWithdrawalDate();//30-04-2022
                var movementDate = fixedDepositAccountMovement.getMovementDate();//01/05/2022

                var deposito = Mono.just("D".equals(fixedDepositAccountMovement.getMovementType()))
                        .flatMap(isDeposit -> {
                            if (isDeposit) {
                                return Mono.just(Boolean.TRUE);
                            } else {
                                var balance = fixedDepositAccountMovementService.getBalanceByAccount(acc.getAccountNumber())
                                        .map(amount -> fixedDepositAccountMovement.getAmount() <= amount)
                                        .map(ok -> ok &&  movementDate.before(withdrawalDate));
                                return balance;
                            }
                        });
                return deposito;
            } catch (Exception ex) {
                return Mono.just(Boolean.FALSE);
            }

        }).flatMap(ok -> {
            if ( ok ) {
                return fixedDepositAccountMovementService.create(fixedDepositAccountMovement);
            }
            return Mono.just(new FixedDepositAccountMovement());
        });
        return _ok;
    }


    @DeleteMapping("/movement")
    public Mono<FixedDepositAccountMovement> delete(@RequestBody FixedDepositAccountMovement fixedDepositAccountMovement){
        return fixedDepositAccountMovementService.delete(fixedDepositAccountMovement);
    }

    @DeleteMapping("movement/byId/{id}")
    public Mono<FixedDepositAccountMovement> deleteMovementById(@RequestBody String id){
        return fixedDepositAccountMovementService.deleteById(id);
    }

    @GetMapping("/accountBalance/{account}")
    public Mono<Double> getAccountBalance(@PathVariable("account") String account){
        return fixedDepositAccountMovementService.getBalanceByAccount(account);
    }


}