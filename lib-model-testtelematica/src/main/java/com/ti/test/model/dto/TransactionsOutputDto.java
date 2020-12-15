package com.ti.test.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ti.test.model.api.TransactionApi;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TransactionsOutputDto {

    @JsonProperty(value = "list")
    private List<TransactionApi> list;
}
