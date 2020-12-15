package com.ti.test.connector.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ti.test.exception.InternalException;
import com.ti.test.model.api.response.BankTransferResponse;
import com.ti.test.model.api.response.BaseApiResponse;
import com.ti.test.model.dto.BankTransferInputDto;
import com.ti.test.model.dto.BankTransferOutputDto;
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

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class BankTransferRestConnectorTest {

    @InjectMocks
    private BankTransferRestConnector connector;
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
        given(restTemplate.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), eq(BankTransferResponse.class)))
                .willReturn(mockResponse(true));
        //when
        BankTransferOutputDto output = connector.call(mockInput());
        //then
        Assert.assertNotNull(output);
        Assert.assertTrue(output.getTransferSuccessful());
    }

    @Test
    public void call_shouldHandleError() throws InternalException {
        //given
        given(restTemplateBuilder.setConnectTimeout(any(Duration.class))).willReturn(restTemplateBuilder);
        given(restTemplateBuilder.setReadTimeout(any(Duration.class))).willReturn(restTemplateBuilder);
        given(restTemplateBuilder.errorHandler(any(ResponseErrorHandler.class))).willReturn(restTemplateBuilder);
        given(restTemplateBuilder.build()).willReturn(restTemplate);
        given(restTemplate.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), eq(BankTransferResponse.class)))
                .willReturn(mockResponse(false));
        //when
        //then
        InternalException ex = assertThrows(InternalException.class, () -> connector.call(mockInput()));
        Assert.assertNotNull(ex);
    }

    private ResponseEntity<BankTransferResponse> mockResponse(boolean statusOk) {

        BankTransferResponse responseBody = new BankTransferResponse();
        if (statusOk) {
            responseBody.setStatus(BankTransferRestConnector.STATUS_OK);
        } else {
            responseBody.setStatus("KO");
            BaseApiResponse.ErrorTemplate err = new BaseApiResponse.ErrorTemplate();
            err.setCode("API000");
            responseBody.setErrors(Collections.singletonList(err));
        }
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    private BankTransferInputDto mockInput() {
        BankTransferInputDto input = new BankTransferInputDto();
        input.setAccountId(BigInteger.ONE);
        input.setCurrency("EUR");
        input.setAmount(BigDecimal.ONE);
        input.setReceiverName("John Doe");
        input.setExecutionDate(LocalDate.now());
        return input;
    }
}