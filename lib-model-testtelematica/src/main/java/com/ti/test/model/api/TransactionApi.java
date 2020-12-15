package com.ti.test.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransactionApi {

    @JsonProperty(value = "transactionId")
    private String transactionId;

    @JsonProperty(value = "operationId")
    private String operationId;

    @JsonDeserialize
    @JsonProperty(value = "accountingDate")
    private LocalDate accountingDate;

    @JsonDeserialize
    @JsonProperty(value = "valueDate")
    private LocalDate valueDate;

    @JsonProperty(value = "amount")
    private BigDecimal amount;

    @JsonProperty(value = "currency")
    private String currency;

    @JsonProperty(value = "description")
    private String description;
}

