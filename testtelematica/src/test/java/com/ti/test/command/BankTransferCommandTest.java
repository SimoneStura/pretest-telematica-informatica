package com.ti.test.command;

import com.ti.test.exception.InternalException;
import com.ti.test.model.dto.BankTransferInputDto;
import com.ti.test.model.dto.BankTransferOutputDto;
import com.ti.test.service.BankTransferService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class BankTransferCommandTest {

    @InjectMocks
    private BankTransferCommand command;
    @Mock
    private BankTransferService service;

    @Test
    public void execute() throws InternalException {
        //given
        BankTransferInputDto inputDto = buildInput();
        command.instantiateCommand(inputDto);
        given(service.makeBankTransfer(inputDto)).willReturn(new BankTransferOutputDto());
        //when
        BankTransferOutputDto output = command.execute();
        //then
        Assert.assertNotNull(output);
        verify(service).makeBankTransfer(inputDto);
    }

    @Test
    public void execute_shouldThrowException() throws InternalException {
        //given
        BankTransferInputDto inputDto = buildInput();
        inputDto.setAccountId(null);
        command.instantiateCommand(inputDto);
        //when
        //then
        InternalException e = assertThrows(InternalException.class, command::execute);
        Assert.assertEquals(command.getExceptionMessage(), e.getMessage());
        verifyNoMoreInteractions(service);
    }

    private BankTransferInputDto buildInput() {
        BankTransferInputDto input = new BankTransferInputDto();
        input.setAccountId(BigInteger.ONE);
        input.setExecutionDate(LocalDate.now());
        input.setAmount(BigDecimal.TEN);
        input.setCurrency("EUR");
        input.setReceiverName("Foo Bar");
        input.setDescription("description");
        input.setReceiverAccountCode("IT000000123456");
        return input;
    }
}