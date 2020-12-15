package com.ti.test.connector.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ti.test.exception.CallingRestException;
import com.ti.test.exception.InternalException;
import com.ti.test.model.api.response.AccountBalanceResponse;
import com.ti.test.model.api.response.BaseApiResponse;
import com.ti.test.model.dto.AccountInputDto;
import com.ti.test.model.dto.AccountOutputDto;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AccountBalanceRestConnectorTest {

    @InjectMocks
    private AccountBalanceRestConnector connector;
    @Mock
    private RestTemplateBuilder restTemplateBuilder;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ObjectMapper objectMapper;

    @Test
    public void call_shouldExecute() throws InternalException {
        //given
        given(restTemplateBuilder.setConnectTimeout(any(Duration.class))).willReturn(restTemplateBuilder);
        given(restTemplateBuilder.setReadTimeout(any(Duration.class))).willReturn(restTemplateBuilder);
        given(restTemplateBuilder.errorHandler(any(ResponseErrorHandler.class))).willReturn(restTemplateBuilder);
        given(restTemplateBuilder.build()).willReturn(restTemplate);
        given(restTemplate.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), eq(AccountBalanceResponse.class)))
                .willReturn(mockResponse(true));
        //when
        AccountOutputDto output = connector.call(mockInput());
        //then
        Assert.assertNotNull(output);
        Assert.assertNotNull(output.getBalance());
        Assert.assertNotNull(output.getAvailableBalance());
        Assert.assertNotNull(output.getCurrency());
        Assert.assertNotNull(output.getDate());
    }

    @Test
    public void call_shouldThrowException() throws InternalException {
        //given
        given(restTemplateBuilder.setConnectTimeout(any(Duration.class))).willReturn(restTemplateBuilder);
        given(restTemplateBuilder.setReadTimeout(any(Duration.class))).willReturn(restTemplateBuilder);
        given(restTemplateBuilder.errorHandler(any(ResponseErrorHandler.class))).willReturn(restTemplateBuilder);
        given(restTemplateBuilder.build()).willReturn(restTemplate);
        given(restTemplate.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), eq(AccountBalanceResponse.class)))
                .willReturn(mockResponse(false));
        //when
        //then
        InternalException ex = assertThrows(InternalException.class, () -> connector.call(mockInput()));
        Assert.assertNotNull(ex);
    }

    private ResponseEntity<AccountBalanceResponse> mockResponse(boolean statusOk) {

        AccountBalanceResponse responseBody = new AccountBalanceResponse();
        if (statusOk) {
            responseBody.setStatus("OK");
            AccountOutputDto payload = new AccountOutputDto();
            payload.setBalance(BigDecimal.ONE);
            payload.setAvailableBalance(BigDecimal.TEN);
            payload.setDate(LocalDate.now());
            payload.setCurrency("EUR");
            responseBody.setPayload(payload);
        } else {
            responseBody.setStatus("KO");
            BaseApiResponse.ErrorTemplate err = new BaseApiResponse.ErrorTemplate();
            err.setCode("API000");
            responseBody.setErrors(Collections.singletonList(err));
        }
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    private AccountInputDto mockInput() {
        AccountInputDto input = new AccountInputDto();
        input.setAccountId(BigInteger.ONE);
        return input;
    }
}