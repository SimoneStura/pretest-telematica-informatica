package com.ti.test.service;

import com.ti.test.connector.rest.TransactionsRestConnector;
import com.ti.test.exception.CallingRestException;
import com.ti.test.exception.InternalException;
import com.ti.test.model.dto.TransactionsInputDto;
import com.ti.test.model.dto.TransactionsOutputDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionsService {

    @Autowired
    private TransactionsRestConnector connector;

    public TransactionsOutputDto retrieveTransactions(TransactionsInputDto inputDto) throws InternalException {
        try {
            return connector.call(inputDto);
        } catch(CallingRestException ex) {
            ex.getErrors().forEach(error ->
                    log.error("code: {} - description: {} - params: {}", error.getCode(), error.getDescription(), error.getParams())
            );
            throw ex;
        }
    }
}
