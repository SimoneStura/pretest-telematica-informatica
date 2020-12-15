package com.ti.test.connector.rest;

import com.ti.test.exception.CallingRestException;
import com.ti.test.exception.InternalException;
import com.ti.test.model.api.response.AccountBalanceResponse;
import com.ti.test.model.dto.AccountInputDto;
import com.ti.test.model.dto.AccountOutputDto;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AccountBalanceRestConnector extends BaseRestConnector<Void, AccountOutputDto> {

    private static final String ACCOUNT_BALANCE_URL = "/api/gbs/banking/v4.0/accounts/{accountId}/balance";
    private static final String RESPONSE_STATUS_OK = "OK";

    public AccountOutputDto call(AccountInputDto request) throws InternalException {
        RestConfiguration configuration = new RestConfiguration();
        configuration.setMethod(HttpMethod.GET);
        configuration.setUrl(ACCOUNT_BALANCE_URL);
        configuration.setUriParams(Collections.singletonMap("accountId", request.getAccountId().toString()));
        ResponseEntity<AccountBalanceResponse> responseEntity = doCall(configuration, null, AccountBalanceResponse.class);
        return handleResponse(responseEntity);
    }

    private AccountOutputDto handleResponse(ResponseEntity<AccountBalanceResponse> responseEntity) throws InternalException {
        AccountBalanceResponse body = responseEntity.getBody();
        if (body != null && RESPONSE_STATUS_OK.equals(body.getStatus())) {
            return body.getPayload();
        }
        throw new InternalException("Impossibile ritrovare le info dell'account selezionato");
    }
}
