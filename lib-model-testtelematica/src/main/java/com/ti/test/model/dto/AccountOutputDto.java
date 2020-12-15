package com.ti.test.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class AccountOutputDto {

    @JsonDeserialize()
    @JsonProperty(value = "date")
    private LocalDate date;

    @JsonProperty(value = "balance")
    private BigDecimal balance;

    @JsonProperty(value = "availableBalance")
    private BigDecimal availableBalance;

    @JsonProperty(value = "currency")
    private String currency;
}
