package com.ti.test;

import com.ti.test.exception.CallingRestException;
import com.ti.test.model.api.response.BaseApiResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static java.util.Collections.singletonList;

public class TestUtils {

    private TestUtils() {}

    public static void mockConsoleInput(String... givenInput) {
        String inputString = String.join("\n", givenInput);
        InputStream streamIn = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(streamIn);
    }

    public static InputStream buildInputStream(String... givenInput) {
        String inputString = String.join("\n", givenInput);
        return new ByteArrayInputStream(inputString.getBytes());
    }

    public static CallingRestException mockCallingRestException() {
        BaseApiResponse.ErrorTemplate error = new BaseApiResponse.ErrorTemplate();
        error.setCode("0001");
        error.setDescription("description");
        error.setParams("params");
        CallingRestException restException = new CallingRestException("message");
        restException.setErrors(singletonList(error));
        return restException;
    }
}
