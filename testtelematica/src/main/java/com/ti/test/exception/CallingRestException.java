package com.ti.test.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import com.ti.test.model.api.response.BaseApiResponse;
import lombok.Setter;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Getter
@Setter
public class CallingRestException extends RestClientException {

    @JsonProperty(value = "errors")
    private List<BaseApiResponse.ErrorTemplate> errors;

    public CallingRestException(String msg) {
        super(msg);
    }

    public CallingRestException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
