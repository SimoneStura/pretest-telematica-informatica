package com.ti.test.presentation;

import com.ti.test.model.constants.PresentationChoice;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MainPresentation {

    private static final String QUITTING_CHARACTER = "Q";
    private Scanner scanner;

    @Autowired
    private BeanFactory beanFactory;

    public void present() {

        scanner = new Scanner(System.in);
        boolean canContinueExecution = true;
        do {
            System.out.println();
            System.out.println("Scegliere un'operazione tra le seguenti");
            System.out.println();
            showOptions();
            System.out.println(QUITTING_CHARACTER + " per uscire dall'applicazione");
            String answer = takeTheAnswer();
            canContinueExecution = executeUserWill(answer);
        } while (canContinueExecution);

        showExitMessage();
        scanner.close();
    }

    private boolean executeUserWill(String answer) {

        boolean canContinueExecution = !QUITTING_CHARACTER.equalsIgnoreCase(answer);

        if (canContinueExecution) {
            PresentationChoice chosen = PresentationChoice.getByValue(answer);
            if (chosen == null) {
                showInputErrorMessage();
            } else {
                BasePresentation nextOperation = null;
                switch (chosen) {
                    case ACCOUNT:
                        nextOperation = beanFactory.getBean(AccountBalancePresentation.class);
                        break;
                    case TRANSACTIONS:
                        nextOperation = beanFactory.getBean(TransactionsPresentation.class);
                        break;
                    case BANK_TRANSFER:
                        nextOperation = beanFactory.getBean(BankTransferPresentation.class);
                        break;
                    default:
                        showInputErrorMessage();
                }

                if(nextOperation != null) {
                    nextOperation.setScanner(scanner);
                    nextOperation.present();
                }
            }
        }

        return canContinueExecution;
    }

    private void showInputErrorMessage() {
        System.out.println("Opzione non valida, si prega di riprovare");
        System.out.println("================================");
    }

    private void showOptions() {
        StringBuilder builder = new StringBuilder();
        for (PresentationChoice possibleChoice : PresentationChoice.values()) {
            builder.append("\t");
            builder.append(possibleChoice.getValue());
            builder.append(": ");
            builder.append(possibleChoice.getDescription());
            builder.append("\n");
        }

        System.out.println(builder);
    }

    private String takeTheAnswer() {
        return scanner.nextLine();
    }

    private void showExitMessage() {
        System.out.println();
        System.out.println("Arrivederci!");
    }
}
