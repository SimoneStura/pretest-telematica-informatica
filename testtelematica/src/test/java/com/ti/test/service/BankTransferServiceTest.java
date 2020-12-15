package com.ti.test.service;

import com.ti.test.TestUtils;
import com.ti.test.connector.rest.BankTransferRestConnector;
import com.ti.test.exception.CallingRestException;
import com.ti.test.exception.InternalException;
import com.ti.test.model.api.response.BaseApiResponse;
import com.ti.test.model.dto.BankTransferInputDto;
import com.ti.test.model.dto.BankTransferOutputDto;
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
public class BankTransferServiceTest {

    @InjectMocks
    private BankTransferService service;
    @Mock
    private BankTransferRestConnector connector;

    @Test
    public void retrieveBalance_shouldExecute() throws InternalException {
        //given
        given(connector.call(any(BankTransferInputDto.class))).willReturn(new BankTransferOutputDto());
        //when
        BankTransferOutputDto output = service.makeBankTransfer(new BankTransferInputDto());
        //then
        Assert.assertNotNull(output);
    }

    @Test
    public void retrieveBalance_shouldThrowException() throws InternalException {
        //given
        given(connector.call(any(BankTransferInputDto.class))).willThrow(TestUtils.mockCallingRestException());
        //when
        CallingRestException ex = assertThrows(CallingRestException.class, () -> service.makeBankTransfer(new BankTransferInputDto()));
        //then
        Assert.assertNotNull(ex.getErrors());
        Assert.assertEquals(1, ex.getErrors().size());
    }
}