package com.ti.test.service;

import com.ti.test.TestUtils;
import com.ti.test.connector.rest.TransactionsRestConnector;
import com.ti.test.exception.CallingRestException;
import com.ti.test.exception.InternalException;
import com.ti.test.model.api.response.BaseApiResponse;
import com.ti.test.model.dto.TransactionsInputDto;
import com.ti.test.model.dto.TransactionsOutputDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class TransactionsServiceTest {

    @InjectMocks
    private TransactionsService service;
    @Mock
    private TransactionsRestConnector connector;

    @Test
    public void retrieveBalance_shouldExecute() throws InternalException {
        //given
        given(connector.call(any(TransactionsInputDto.class))).willReturn(new TransactionsOutputDto());
        //when
        TransactionsOutputDto output = service.retrieveTransactions(new TransactionsInputDto());
        //then
        Assert.assertNotNull(output);
    }

    @Test
    public void retrieveBalance_shouldThrowException() throws InternalException {
        //given
        given(connector.call(any(TransactionsInputDto.class))).willThrow(TestUtils.mockCallingRestException());
        //when
        CallingRestException ex = assertThrows(CallingRestException.class, () -> service.retrieveTransactions(new TransactionsInputDto()));
        //then
        Assert.assertNotNull(ex.getErrors());
        Assert.assertEquals(1, ex.getErrors().size());
    }
}