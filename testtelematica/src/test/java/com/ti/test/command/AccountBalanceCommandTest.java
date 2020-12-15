package com.ti.test.command;

import com.ti.test.exception.InternalException;
import com.ti.test.service.AccountService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.ti.test.model.dto.AccountInputDto;
import com.ti.test.model.dto.AccountOutputDto;

import java.math.BigInteger;

import static org.junit.Assert.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class AccountBalanceCommandTest {

    @InjectMocks
    private AccountBalanceCommand command;
    @Mock
    private AccountService service;

    @Test
    public void execute() throws InternalException {
        //given
        AccountInputDto inputDto = buildInput();
        command.instantiateCommand(inputDto);
        given(service.retrieveBalance(inputDto)).willReturn(new AccountOutputDto());
        //when
        AccountOutputDto output = command.execute();
        //then
        Assert.assertNotNull(output);
        verify(service).retrieveBalance(inputDto);
    }

    @Test
    public void execute_shouldThrowException() throws InternalException {
        //given
        AccountInputDto inputDto = buildInput();
        inputDto.setAccountId(null);
        command.instantiateCommand(inputDto);
        //when
        //then
        InternalException e = assertThrows(InternalException.class, command::execute);
        Assert.assertEquals(command.getExceptionMessage(), e.getMessage());
        verifyNoMoreInteractions(service);
    }

    private AccountInputDto buildInput() {
        AccountInputDto input = new AccountInputDto();
        input.setAccountId(BigInteger.ONE);
        return input;
    }
}