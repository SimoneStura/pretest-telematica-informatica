package com.ti.test.connector.rest;

import com.ti.test.exception.CallingRestException;
import com.ti.test.exception.InternalException;
import com.ti.test.model.api.response.TransactionsResponse;
import com.ti.test.model.dto.TransactionsInputDto;
import com.ti.test.model.dto.TransactionsOutputDto;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.Optional;

import static java.time.format.DateTimeFormatter.ISO_DATE;

@Component
public class TransactionsRestConnector extends BaseRestConnector<Void, TransactionsResponse> {

    private static final String TRANSACTIONS_URL = "/api/gbs/banking/v4.0/accounts/{accountId}/transactions";
    private static final String RESPONSE_STATUS_OK = "OK";
    private static final String QUERYPARAM_FROM_DATE = "fromAccountingDate";
    private static final String QUERYPARAM_TO_DATE = "toAccountingDate";

    public TransactionsOutputDto call(TransactionsInputDto request) throws InternalException {
        RestConfiguration configuration = new RestConfiguration();
        configuration.setMethod(HttpMethod.GET);
        configuration.setUrl(TRANSACTIONS_URL);
        configuration.setUriParams(Collections.singletonMap("accountId", request.getAccountId().toString()));
        configuration.setQueryParams(buildQueryParams(request));
        ResponseEntity<TransactionsResponse> responseEntity = doCall(configuration, null, TransactionsResponse.class);
        return handleResponse(responseEntity);
    }

    private MultiValueMap<String, String> buildQueryParams(TransactionsInputDto request) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(QUERYPARAM_FROM_DATE, Collections.singletonList(Optional.ofNullable(request.getFromAccountingDate())
                .map(ISO_DATE::format)
                .orElse(Strings.EMPTY)));
        headers.put(QUERYPARAM_TO_DATE, Collections.singletonList(Optional.ofNullable(request.getToAccountingDate())
                .map(ISO_DATE::format)
                .orElse(Strings.EMPTY)));
        return headers;
    }

    private TransactionsOutputDto handleResponse(ResponseEntity<TransactionsResponse> responseEntity) throws InternalException {
        TransactionsResponse body = responseEntity.getBody();
        if (body != null && RESPONSE_STATUS_OK.equals(body.getStatus())) {
            return body.getPayload();
        }
        throw new InternalException("Impossibile ritrovare le transazioni dell'account selezionato");
    }
}
