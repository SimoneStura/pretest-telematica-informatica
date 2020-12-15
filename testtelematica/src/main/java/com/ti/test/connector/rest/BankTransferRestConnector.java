package com.ti.test.connector.rest;

import com.google.common.collect.Iterables;
import com.ti.test.exception.InternalException;
import com.ti.test.model.api.*;
import com.ti.test.model.api.request.BankTransferRequest;
import com.ti.test.model.api.response.BankTransferResponse;
import com.ti.test.model.api.response.BaseApiResponse;
import com.ti.test.model.dto.BankTransferInputDto;
import com.ti.test.model.dto.BankTransferOutputDto;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;

@Component
public class BankTransferRestConnector extends BaseRestConnector<BankTransferRequest, BankTransferResponse> {

    static final String STATUS_OK = "OK";
    private static final String BANK_TRANSFER_URL = "/api/gbs/banking/v4.0/accounts/{accountId}/payments/money-transfers";
    private static final DateTimeFormatter CUSTOM_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public BankTransferOutputDto call(BankTransferInputDto inputDto) throws InternalException {

        RestConfiguration configuration = new RestConfiguration();
        configuration.setMethod(HttpMethod.POST);
        configuration.setUrl(BANK_TRANSFER_URL);
        configuration.setUriParams(Collections.singletonMap("accountId", inputDto.getAccountId().toString()));
        BankTransferRequest request = buildRequest(inputDto);
        ResponseEntity<BankTransferResponse> responseEntity = doCall(configuration, request, BankTransferResponse.class);
        return handleResponse(responseEntity);
    }

    private BankTransferRequest buildRequest(BankTransferInputDto inputDto) {
        BankTransferRequest request = new BankTransferRequest();
        request.setAmount(inputDto.getAmount());
        request.setCurrency(inputDto.getCurrency());
        request.setDescription(inputDto.getDescription());
        request.setFeeAccountId(inputDto.getAccountId().toString());
        request.setExecutionDate(Optional.ofNullable(inputDto.getExecutionDate())
                .map(CUSTOM_DATE_FORMATTER::format)
                .orElse(Strings.EMPTY));
        CreditorApi creditor = new CreditorApi();
        creditor.setName(inputDto.getReceiverName());
        AccountApi account = new AccountApi();
        account.setAccountCode(inputDto.getReceiverAccountCode());
        creditor.setAccount(account);
        request.setCreditor(creditor);
        return request;
    }

    private BankTransferOutputDto handleResponse(ResponseEntity<BankTransferResponse> response) throws InternalException {
        BankTransferResponse body = response.getBody();
        if (body != null && STATUS_OK.equals(body.getStatus())) {
            BankTransferOutputDto outputDto = new BankTransferOutputDto();
            outputDto.setTransferSuccessful(true);
            return outputDto;
        }
        throw new InternalException("Errore nella chiamata esterna");
    }
}
