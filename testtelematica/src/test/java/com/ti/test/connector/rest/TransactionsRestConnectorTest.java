package com.ti.test.connector.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ti.test.exception.CallingRestException;
import com.ti.test.exception.InternalException;
import com.ti.test.model.api.TransactionApi;
import com.ti.test.model.api.response.TransactionsResponse;
import com.ti.test.model.api.response.BaseApiResponse;
import com.ti.test.model.dto.TransactionsInputDto;
import com.ti.test.model.dto.TransactionsOutputDto;
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
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class TransactionsRestConnectorTest {

    @InjectMocks
    private TransactionsRestConnector connector;
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
        given(restTemplate.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), eq(TransactionsResponse.class)))
                .willReturn(mockResponse(true));
        //when
        TransactionsOutputDto output = connector.call(mockInput());
        //then
        Assert.assertNotNull(output);
        List<TransactionApi> list = output.getList();
        Assert.assertNotNull(list);
        Assertions.assertThat(list).hasSize(1);
    }

    @Test
    public void call_shouldThrowException() throws InternalException {
        //given
        given(restTemplateBuilder.setConnectTimeout(any(Duration.class))).willReturn(restTemplateBuilder);
        given(restTemplateBuilder.setReadTimeout(any(Duration.class))).willReturn(restTemplateBuilder);
        given(restTemplateBuilder.errorHandler(any(ResponseErrorHandler.class))).willReturn(restTemplateBuilder);
        given(restTemplateBuilder.build()).willReturn(restTemplate);
        given(restTemplate.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), eq(TransactionsResponse.class)))
                .willReturn(mockResponse(false));
        //when
        //then
        InternalException ex = assertThrows(InternalException.class, () -> connector.call(mockInput()));
        Assert.assertNotNull(ex);
    }

    private ResponseEntity<TransactionsResponse> mockResponse(boolean statusOk) {

        TransactionsResponse responseBody = new TransactionsResponse();
        if (statusOk) {
            responseBody.setStatus("OK");
            TransactionsOutputDto payload = new TransactionsOutputDto();
            TransactionApi transaction = new TransactionApi();
            transaction.setTransactionId("123456");
            transaction.setOperationId("456789");
            transaction.setAmount(BigDecimal.valueOf(123.45));
            transaction.setCurrency("EUR");
            transaction.setDescription("asdasd");
            transaction.setAccountingDate(LocalDate.of(2019,3,2));
            payload.setList(Collections.singletonList(transaction));
            responseBody.setPayload(payload);
        } else {
            responseBody.setStatus("KO");
            BaseApiResponse.ErrorTemplate err = new BaseApiResponse.ErrorTemplate();
            err.setCode("API000");
            responseBody.setErrors(Collections.singletonList(err));
        }
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    private TransactionsInputDto mockInput() {
        TransactionsInputDto input = new TransactionsInputDto();
        input.setAccountId(BigInteger.ONE);
        input.setFromAccountingDate(LocalDate.of(2019,1,1));
        input.setToAccountingDate(LocalDate.of(2019,12,31));
        return input;
    }
}