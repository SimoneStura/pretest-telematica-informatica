package com.ti.test.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
public class BankTransferInputDto {

    private BigInteger accountId;
    private String description;
    private String currency;
    private BigDecimal amount;
    private LocalDate executionDate;
    private String receiverName;
    private String receiverAccountCode;
}
