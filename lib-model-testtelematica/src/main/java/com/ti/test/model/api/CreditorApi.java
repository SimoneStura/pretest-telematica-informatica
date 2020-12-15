package com.ti.test.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditorApi {

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "account")
    private AccountApi account;
}
