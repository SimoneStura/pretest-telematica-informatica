package com.ti.test.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
public class TransactionsInputDto {

    private BigInteger accountId;
    private LocalDate fromAccountingDate;
    private LocalDate toAccountingDate;
}
