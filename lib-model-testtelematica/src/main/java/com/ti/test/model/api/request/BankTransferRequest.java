package com.ti.test.model.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ti.test.model.api.CreditorApi;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BankTransferRequest {

    @JsonProperty(value = "feeAccountId")
    private String feeAccountId;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "currency")
    private String currency;

    @JsonProperty(value = "amount")
    private BigDecimal amount;

    @JsonProperty(value = "executionDate")
    private String executionDate;

    @JsonProperty(value = "creditor")
    private CreditorApi creditor;
}
