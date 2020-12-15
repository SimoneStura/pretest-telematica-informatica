package com.ti.test.command;

import com.ti.test.exception.InternalException;
import com.ti.test.model.dto.TransactionsInputDto;
import com.ti.test.model.dto.TransactionsOutputDto;
import com.ti.test.service.TransactionsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;

import static org.junit.Assert.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class TransactionsCommandTest {

    @InjectMocks
    private TransactionsCommand command;
    @Mock
    private TransactionsService service;

    @Test
    public void execute() throws InternalException {
        //given
        TransactionsInputDto inputDto = buildInput();
        command.instantiateCommand(inputDto);
        given(service.retrieveTransactions(inputDto)).willReturn(new TransactionsOutputDto());
        //when
        TransactionsOutputDto output = command.execute();
        //then
        Assert.assertNotNull(output);
        verify(service).retrieveTransactions(inputDto);
    }

    @Test
    public void execute_shouldThrowException() throws InternalException {
        //given
        TransactionsInputDto inputDto = buildInput();
        inputDto.setAccountId(null);
        command.instantiateCommand(inputDto);
        //when
        //then
        InternalException e = assertThrows(InternalException.class, command::execute);
        Assert.assertEquals(command.getExceptionMessage(), e.getMessage());
        verifyNoMoreInteractions(service);
    }

    private TransactionsInputDto buildInput() {
        TransactionsInputDto input = new TransactionsInputDto();
        input.setAccountId(BigInteger.ONE);
        return input;
    }
}