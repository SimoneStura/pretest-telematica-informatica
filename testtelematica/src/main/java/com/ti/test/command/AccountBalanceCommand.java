package com.ti.test.command;

import com.ti.test.exception.InternalException;
import com.ti.test.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.ti.test.model.dto.AccountInputDto;
import com.ti.test.model.dto.AccountOutputDto;

import java.util.Optional;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AccountBalanceCommand extends BaseCommand<AccountOutputDto> {

    private static final String MSG_NO_ACCOUNTID_PROVIDED = "Account id non inserito";

    @Autowired
    private AccountService service;

    private AccountInputDto inputDto;

    public void instantiateCommand(AccountInputDto inputDto) {
        this.inputDto = inputDto;
    }

    @Override
    protected boolean canExecute() {
        return Optional.ofNullable(inputDto)
                .map(AccountInputDto::getAccountId)
                .isPresent();
    }

    @Override
    protected AccountOutputDto doExecute() throws InternalException {
        return service.retrieveBalance(inputDto);
    }

    @Override
    protected String getExceptionMessage() {
        return MSG_NO_ACCOUNTID_PROVIDED;
    }
}
