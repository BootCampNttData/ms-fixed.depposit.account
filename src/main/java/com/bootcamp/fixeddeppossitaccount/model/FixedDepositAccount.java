package com.bootcamp.fixeddeppossitaccount.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class FixedDepositAccount {
    @Id
    private String  id;
    private Integer accountNumber;
    private Date    depositDate;
    private Date    withdrawalDate;
    private Double  percent;
    private String  clientId;
}
