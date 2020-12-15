package com.ti.test.presentation;

import com.ti.test.command.BankTransferCommand;
import com.ti.test.exception.CallingRestException;
import com.ti.test.exception.InternalException;
import com.ti.test.model.dto.BankTransferInputDto;
import com.ti.test.model.dto.BankTransferOutputDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@Component
public class BankTransferPresentation extends BasePresentation {

    private static final BigInteger FIXED_ACCOUNT_ID = BigInteger.valueOf(14537780);
    public static final String FIXED_RECEIVER_NAME = "John Doe";
    public static final String FIXED_RECEIVER_ACCOUNT_CODE = "IT23A0336844430152923804660";
    public static final String FIXED_CURRENCY = "EUR";

    @Autowired
    private BeanFactory beanFactory;

    @Override
    protected void showContent() {
        BankTransferInputDto inputDto = new BankTransferInputDto();
        setFixedValues(inputDto);
        inputDto.setAmount(obtainDecimal("importo in EURO"));
        inputDto.setDescription(obtainString("descrizione"));
        System.out.println("Avvio richiesta di bonifico per il conto n. " + inputDto.getAccountId());
        BankTransferCommand command = initializeCommand(inputDto);
        try {
            BankTransferOutputDto outputDto = command.execute();
            System.out.println("Fine richiesta di bonifico per il conto n. " + inputDto.getAccountId());
            System.out.println();
            showResult(outputDto);
        } catch (CallingRestException restEx) {

            System.out.println("Transazione annullata.");
            restEx.getErrors().forEach(CommonPresentation::showErrorDetails);
        } catch (InternalException e) {

            log.error("ERROR!", e);
            System.out.println();
            System.out.println("!! ERRORE !!");
            System.out.println(e.getMessage());
        }
    }

    private String obtainString(String valueName) {
        String insertedValue = null;
        do {
            System.out.println("Inserire " + valueName + ":");
            insertedValue = scanner.nextLine();
            if (StringUtils.isBlank(insertedValue)) {
                System.out.println("Il campo non può essere vuoto!");
                System.out.println();
            }
        } while (StringUtils.isBlank(insertedValue));
        return insertedValue;
    }

    private BigDecimal obtainDecimal(String valueName) {
        BigDecimal parsedDecimal = null;
        do {
            System.out.println("Inserire " + valueName + ":");
            String insertedValue = scanner.nextLine();
            try {
                parsedDecimal = new BigDecimal(insertedValue);
            } catch (NumberFormatException ex) {
                parsedDecimal = null;
                System.out.println("Valore non valido");
                System.out.println();
            }
        } while (parsedDecimal == null);

        return parsedDecimal;
    }

    private void setFixedValues(BankTransferInputDto inputDto) {
        inputDto.setAccountId(FIXED_ACCOUNT_ID);
        inputDto.setReceiverName(FIXED_RECEIVER_NAME);
        inputDto.setReceiverAccountCode(FIXED_RECEIVER_ACCOUNT_CODE);
        inputDto.setCurrency(FIXED_CURRENCY);
    }

    private void showResult(BankTransferOutputDto outputDto) {

        if (BooleanUtils.isTrue(outputDto.getTransferSuccessful())) {
            System.out.println("Transazione effettuata correttamente");
        } else {
            System.out.println("Transazione annullata");
        }
    }

    private BankTransferCommand initializeCommand(BankTransferInputDto inputDto) {
        BankTransferCommand command = beanFactory.getBean(BankTransferCommand.class);
        command.instantiateCommand(inputDto);
        return command;
    }

    @Override
    protected void showEndingContent() {
        System.out.println("FINE OPERAZIONE");
    }
}
