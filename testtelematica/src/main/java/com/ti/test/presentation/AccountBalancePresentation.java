package com.ti.test.presentation;

import com.ti.test.command.AccountBalanceCommand;
import com.ti.test.exception.CallingRestException;
import com.ti.test.exception.InternalException;
import com.ti.test.model.dto.AccountInputDto;
import com.ti.test.model.dto.AccountOutputDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Slf4j
@Component
public class AccountBalancePresentation extends BasePresentation {

    private static final BigInteger FIXED_ACCOUNT_ID = BigInteger.valueOf(14537780);

    @Autowired
    private BeanFactory beanFactory;

    @Override
    protected void showContent() {
        AccountInputDto inputDto = new AccountInputDto();
        inputDto.setAccountId(FIXED_ACCOUNT_ID);
        System.out.println("Avvio recupero del saldo per il conto n. " + inputDto.getAccountId());
        AccountBalanceCommand command = initializeCommand(inputDto);
        try {
            AccountOutputDto outputDto = command.execute();
            System.out.println("Fine recupero del saldo per il conto n. " + inputDto.getAccountId());
            System.out.println();
            showResult(outputDto);
        } catch (CallingRestException restEx) {

            System.out.println("Errore nel recupero del saldo");
            restEx.getErrors().forEach(CommonPresentation::showErrorDetails);
        } catch (InternalException e) {

            log.error("ERROR!", e);
            System.out.println();
            System.out.println("!! ERRORE !!");
            System.out.println(e.getMessage());
        }
    }

    private void showResult(AccountOutputDto outputDto) {
        System.out.println("Data: " + outputDto.getDate());
        System.out.println("Saldo: " + outputDto.getBalance() + " " + outputDto.getCurrency());
        System.out.println("Saldo disponibile: " + outputDto.getAvailableBalance() + " " + outputDto.getCurrency());
    }

    private AccountBalanceCommand initializeCommand(AccountInputDto inputDto) {
        AccountBalanceCommand command = beanFactory.getBean(AccountBalanceCommand.class);
        command.instantiateCommand(inputDto);
        return command;
    }

    @Override
    protected void showEndingContent() {
        System.out.println("FINE VISUALIZZAZIONE SALDO");
    }
}
