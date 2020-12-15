package com.ti.test.service;

import com.ti.test.TestUtils;
import com.ti.test.exception.InternalException;
import com.ti.test.connector.rest.AccountBalanceRestConnector;
import com.ti.test.exception.CallingRestException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.ti.test.model.api.response.BaseApiResponse;
import com.ti.test.model.dto.AccountInputDto;
import com.ti.test.model.dto.AccountOutputDto;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService service;
    @Mock
    private AccountBalanceRestConnector connector;

    @Test
    public void retrieveBalance_shouldExecute() throws InternalException {
        //given
        given(connector.call(any(AccountInputDto.class))).willReturn(new AccountOutputDto());
        //when
        AccountOutputDto output = service.retrieveBalance(new AccountInputDto());
        //then
        Assert.assertNotNull(output);
    }

    @Test
    public void retrieveBalance_shouldThrowException() throws InternalException {
        //given
        given(connector.call(any(AccountInputDto.class))).willThrow(TestUtils.mockCallingRestException());
        //when
        CallingRestException ex = assertThrows(CallingRestException.class, () -> service.retrieveBalance(new AccountInputDto()));
        //then
        Assert.assertNotNull(ex.getErrors());
        Assert.assertEquals(1, ex.getErrors().size());
    }
}