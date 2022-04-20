package com.bootcamp.fixeddeppossitaccount.controller;

import com.bootcamp.fixeddeppossitaccount.model.FixedDepositAccountMovement;
import com.bootcamp.fixeddeppossitaccount.service.FixedDepositAccountMovementService;
import com.bootcamp.fixeddeppossitaccount.service.FixedDepositAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/fixedDepositAccountMovement")
@RequiredArgsConstructor
public class FixedDepositAccountMovementController {
    @Value("${passiveproducts.server.url}")
    private String passPrdUrl;

    public final FixedDepositAccountMovementService service;

    public final FixedDepositAccountService accountService;


    @GetMapping
    public Flux<FixedDepositAccountMovement> getAll(){
        return service.findAll();
    }

    @GetMapping("/find/{num}")
    public Flux<FixedDepositAccountMovement> getByAccountNumber(@PathVariable("num") Integer num){
        return service.findByAccountNumber(num);
    }

    /**
     * Realiza deposito o retiro de cuenta a plazo fijo
     * @param fixedDepositAccountMovement
     * @return
     */
    @PostMapping
    public Mono<FixedDepositAccountMovement> create(@RequestBody FixedDepositAccountMovement fixedDepositAccountMovement){
        SimpleDateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy");

        Date mvDate=fixedDepositAccountMovement.getMovementDate();
        String mvType=fixedDepositAccountMovement.getMovementType();
//        RestTemplate restTemplate=new RestTemplate();
//        String urlDp = passPrdUrl +"/fixedDepositAccount/find/" + fixedDepositAccountMovement.getAccountNumber();
//        ResponseEntity<FixedDepositAccount[]> fixedDepositAccount = restTemplate.getForEntity(urlDp,FixedDepositAccount[].class);

        var account = accountService.findByAccountNumber(fixedDepositAccountMovement.getAccountNumber());
        var _ok = account.flatMap(acc -> {
            try {
                var withdrawalDate = acc.getWithdrawalDate();//30-04-2022
                var movementDate = fixedDepositAccountMovement.getMovementDate();//01/05/2022

                var deposito = Mono.just("D".equals(fixedDepositAccountMovement.getMovementType()))
                    .flatMap(isDeposit -> {
                        if (isDeposit) {
                            return Mono.just(Boolean.TRUE);
                        } else {
                            var balance = service.getBalanceByAccount(acc.getAccountNumber())
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
                return service.create(fixedDepositAccountMovement);
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

    @PostMapping("/update")
    public Mono<FixedDepositAccountMovement> update(@RequestBody FixedDepositAccountMovement fixedDepositAccountMovement){
        return service.create(fixedDepositAccountMovement);
    }

    @DeleteMapping
    public Mono<FixedDepositAccountMovement> delete(@RequestBody FixedDepositAccountMovement fixedDepositAccountMovement){
        return service.delete(fixedDepositAccountMovement);
    }

    @DeleteMapping("/byId/{id}")
    public Mono<FixedDepositAccountMovement> deleteById(@RequestBody String id){
        return service.deleteById(id);
    }

    @GetMapping("/accountBalance/{account}")
    public Mono<Double> getAccountBalance(@PathVariable("account") Integer account){
        return service.getBalanceByAccount(account);

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