package com.ti.test.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountApi {

    @JsonProperty(value = "accountCode")
    public String accountCode;
}
