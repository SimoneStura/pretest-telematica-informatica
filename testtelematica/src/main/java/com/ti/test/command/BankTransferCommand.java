package com.ti.test.command;

import com.ti.test.exception.InternalException;
import com.ti.test.model.dto.BankTransferInputDto;
import com.ti.test.model.dto.BankTransferOutputDto;
import com.ti.test.service.BankTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BankTransferCommand extends BaseCommand<BankTransferOutputDto> {

    private static final String MSG_MISSING_DATA = "Dati in input mancanti";

    @Autowired
    private BankTransferService service;

    private BankTransferInputDto inputDto;

    public void instantiateCommand(BankTransferInputDto inputDto) {
        this.inputDto = inputDto;
    }

    @Override
    protected boolean canExecute() {
        Optional<BankTransferInputDto> inputOpt = Optional.ofNullable(this.inputDto);
        return inputOpt.map(BankTransferInputDto::getAccountId).isPresent() &&
                inputOpt.map(BankTransferInputDto::getAmount).isPresent() &&
                inputOpt.map(BankTransferInputDto::getCurrency).isPresent() &&
                inputOpt.map(BankTransferInputDto::getReceiverName).isPresent() &&
                inputOpt.map(BankTransferInputDto::getDescription).isPresent() &&
                inputOpt.map(BankTransferInputDto::getReceiverAccountCode).isPresent();
    }

    @Override
    protected BankTransferOutputDto doExecute() throws InternalException {
        return service.makeBankTransfer(inputDto);
    }

    @Override
    protected String getExceptionMessage() {
        return MSG_MISSING_DATA;
    }
}
