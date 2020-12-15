package com.ti.test.presentation;

import com.ti.test.command.TransactionsCommand;
import com.ti.test.exception.CallingRestException;
import com.ti.test.exception.InternalException;
import com.ti.test.model.api.TransactionApi;
import com.ti.test.model.dto.TransactionsInputDto;
import com.ti.test.model.dto.TransactionsOutputDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@Component
public class TransactionsPresentation extends BasePresentation {

    private static final BigInteger FIXED_ACCOUNT_ID = BigInteger.valueOf(14537780);
    private static final DateTimeFormatter CUSTOM_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    private BeanFactory beanFactory;

    @Override
    protected void showContent() {
        TransactionsInputDto inputDto = new TransactionsInputDto();
        inputDto.setAccountId(FIXED_ACCOUNT_ID);
        inputDto.setFromAccountingDate(obtainDate("data inizio ricerca"));
        inputDto.setToAccountingDate(obtainDate("data fine ricerca"));
        System.out.println("Avvio recupero delle transazioni per il conto n. " + inputDto.getAccountId());
        TransactionsCommand command = initializeCommand(inputDto);
        try {
            TransactionsOutputDto outputDto = command.execute();
            System.out.println("Fine recupero delle transazioni per il conto n. " + inputDto.getAccountId());
            System.out.println();
            showResult(outputDto);
        } catch (CallingRestException restEx) {

            System.out.println("Errore nel recupero delle transazioni");
            restEx.getErrors().forEach(CommonPresentation::showErrorDetails);
        } catch (InternalException e) {

            log.error("ERROR!", e);
            System.out.println();
            System.out.println("!! ERRORE !!");
            System.out.println(e.getMessage());
        }
    }

    private void showResult(TransactionsOutputDto outputDto) {
        List<TransactionApi> transactionList = outputDto.getList();
        if (CollectionUtils.isEmpty(transactionList)) {
            System.out.println("Nessuna transazione trovata");
        } else {
            System.out.println("Elenco transazioni");
            for (TransactionApi transaction : transactionList) {
                System.out.println("---");
                System.out.println("id: " + transaction.getTransactionId());
                System.out.println("id operazione: " + transaction.getOperationId());
                System.out.println("Data validità: " + transaction.getValueDate());
                System.out.println("Data contabile: " + transaction.getAccountingDate());
                System.out.println("Importo: " + transaction.getAmount() + " " + transaction.getCurrency());
                System.out.println("Descrizione: " + transaction.getDescription());
                System.out.println();
            }
        }
    }

    private LocalDate obtainDate(String dateName) {
        LocalDate parsedDate = null;
        do {
            System.out.println("Inserire " + dateName + " (dd/MM/yyyy):");
            String insertedDate = scanner.nextLine();
            try {
                parsedDate = LocalDate.parse(insertedDate, CUSTOM_DATE_FORMATTER);
            } catch (DateTimeParseException ex) {
                parsedDate = null;
                System.out.println("Data in formato non corretto");
                System.out.println();
            }
        } while (parsedDate == null);

        return parsedDate;
    }

    private TransactionsCommand initializeCommand(TransactionsInputDto inputDto) {
        TransactionsCommand command = beanFactory.getBean(TransactionsCommand.class);
        command.instantiateCommand(inputDto);
        return command;
    }

    @Override
    protected void showEndingContent() {
        System.out.println("FINE VISUALIZZAZIONE TRANSAZIONI");
    }
}
