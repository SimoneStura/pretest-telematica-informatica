package com.ti.test.service;

import com.ti.test.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import com.ti.test.connector.rest.AccountBalanceRestConnector;
import com.ti.test.exception.CallingRestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ti.test.model.dto.AccountInputDto;
import com.ti.test.model.dto.AccountOutputDto;

@Slf4j
@Service
public class AccountService {

    @Autowired
    private AccountBalanceRestConnector connector;

    public AccountOutputDto retrieveBalance(AccountInputDto input) throws InternalException {
        try {
            return connector.call(input);
        } catch(CallingRestException ex) {
            ex.getErrors().forEach(error ->
                    log.error("code: {} - description: {} - params: {}", error.getCode(), error.getDescription(), error.getParams())
            );
            throw ex;
        }
    }
}
