package com.ti.test.command;

import com.ti.test.exception.InternalException;
import com.ti.test.model.dto.TransactionsInputDto;
import com.ti.test.model.dto.TransactionsOutputDto;
import com.ti.test.service.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TransactionsCommand extends BaseCommand<TransactionsOutputDto> {

    private static final String MSG_NO_ACCOUNTID_PROVIDED = "Account id non inserito";

    @Autowired
    private TransactionsService service;

    private TransactionsInputDto inputDto;

    public void instantiateCommand(TransactionsInputDto inputDto) {
        this.inputDto = inputDto;
    }

    @Override
    protected boolean canExecute() {
        return Optional.ofNullable(inputDto)
                .map(TransactionsInputDto::getAccountId)
                .isPresent();
    }

    @Override
    protected TransactionsOutputDto doExecute() throws InternalException {
        return service.retrieveTransactions(inputDto);
    }

    @Override
    protected String getExceptionMessage() {
        return MSG_NO_ACCOUNTID_PROVIDED;
    }
}
