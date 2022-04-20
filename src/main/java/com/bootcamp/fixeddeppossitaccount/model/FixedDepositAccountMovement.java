package com.bootcamp.fixeddeppossitaccount.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document

public class FixedDepositAccountMovement {
    @Id
    private String  id;
    private Integer accountNumber;
    private String  movementType;
    private Date    movementDate;
    private Double  amount;
}
