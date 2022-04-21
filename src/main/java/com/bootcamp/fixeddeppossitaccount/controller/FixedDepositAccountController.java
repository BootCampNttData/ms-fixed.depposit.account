package com.bootcamp.fixeddeppossitaccount.controller;

import com.bootcamp.fixeddeppossitaccount.model.FixedDepositAccount;
import com.bootcamp.fixeddeppossitaccount.model.FixedDepositAccountMovement;
import com.bootcamp.fixeddeppossitaccount.service.FixedDepositAccountMovementService;
import com.bootcamp.fixeddeppossitaccount.service.FixedDepositAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/fixedDepositAccount")
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
    public Flux<FixedDepositAccountMovement> getByAccountNumber(@PathVariable("num") Integer num){
        return fixedDepositAccountMovementService.findByAccountNumber(num);
    }

    /**
     * Realiza deposito o retiro de cuenta a plazo fijo
     * @param fixedDepositAccountMovement
     * @return
     */
    @PostMapping("/movement")
    public Mono<FixedDepositAccountMovement> create(@RequestBody FixedDepositAccountMovement fixedDepositAccountMovement){
        SimpleDateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy");

        Date mvDate=fixedDepositAccountMovement.getMovementDate();
        String mvType=fixedDepositAccountMovement.getMovementType();
//        RestTemplate restTemplate=new RestTemplate();
//        String urlDp = passPrdUrl +"/fixedDepositAccount/find/" + fixedDepositAccountMovement.getAccountNumber();
//        ResponseEntity<FixedDepositAccount[]> fixedDepositAccount = restTemplate.getForEntity(urlDp,FixedDepositAccount[].class);

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
        //var movements = service.findByAccountNumber(fixedDepositAccountMovement.getAccountNumber());

//        String urlBl = passPrdUrl +"/fixedDepositAccountMovement/accountBalance/" + fixedDepositAccountMovement.getAccountNumber();
//        ResponseEntity<String> balance = restTemplate.getForEntity(urlBl,String.class);

//
//        String wdDate = fixedDepositAccount.getBody()[0].getWithdrawalDate();
//        boolean allowWithdrar=false;
//        try {
//            Date movement = dateFormat.parse(mvDate);
//            Date wihtdrawel = dateFormat.parse(wdDate);
//            if(movement.before(wihtdrawel)){
//                allowWithdrar=true;
//            }
//        }catch (Exception e){}
//
//        if(mvType.equals("D")){
//            return service.create(fixedDepositAccountMovement);
//        }else{
//            /** Se valida el monto de retiro */
//            if(allowWithdrar && Double.parseDouble(fixedDepositAccountMovement.getAmount())<=Double.parseDouble(balance.getBody())){
//                return service.create(fixedDepositAccountMovement);
//            }
//        }
//        return Mono.just(new FixedDepositAccountMovement());
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
    public Mono<Double> getAccountBalance(@PathVariable("account") Integer account){
        return fixedDepositAccountMovementService.getBalanceByAccount(account);

//        String urlDp = passPrdUrl +"/fixedDepositAccountMovement/find/" + account;
//
//
//        ResponseEntity<FixedDepositAccountMovement[]> fixedDepositAccountMovements = restTemplate.getForEntity(urlDp,FixedDepositAccountMovement[].class);
//        for(FixedDepositAccountMovement am: fixedDepositAccountMovements.getBody()){
//            if (am.getMovementType().equals("D")){
//                balance += Double.parseDouble(am.getAmount());
//            }else{
//                balance -= Double.parseDouble(am.getAmount());
//            }
//        }
//        return String.valueOf(balance);
    }


}