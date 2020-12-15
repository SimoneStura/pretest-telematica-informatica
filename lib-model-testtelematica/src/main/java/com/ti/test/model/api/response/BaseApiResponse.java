package com.ti.test.model.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BaseApiResponse<T> {

    @JsonProperty(value = "status")
    private String status;

    @JsonProperty(value = "errors")
    private List<ErrorTemplate> errors;

    @JsonProperty(value = "payload")
    private T payload;

    @Data
    public static class ErrorTemplate {

        @JsonProperty(value = "code")
        private String code;

        @JsonProperty(value = "description")
        private String description;

        @JsonProperty(value = "params")
        private String params;
    }
}
