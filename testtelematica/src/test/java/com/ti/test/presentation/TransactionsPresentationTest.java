package com.ti.test.presentation;

import com.ti.test.TestUtils;
import com.ti.test.command.TransactionsCommand;
import com.ti.test.exception.InternalException;
import com.ti.test.model.api.TransactionApi;
import com.ti.test.model.dto.TransactionsOutputDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.BeanFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Scanner;

import static java.util.Collections.emptyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TransactionsPresentationTest {

    @InjectMocks
    @Spy
    private TransactionsPresentation presentation;
    @Mock
    private BeanFactory beanFactory;
    @Mock
    private TransactionsCommand command;

    @Before
    public void init() {
    }

    @Test
    public void showContent_shouldExecute() throws InternalException {
        //given
        presentation.setScanner(new Scanner(TestUtils.buildInputStream("01/01/2019", "31/12/2019")));
        given(beanFactory.getBean(TransactionsCommand.class)).willReturn(command);
        given(command.execute()).willReturn(mockOutputDto());
        //when
        presentation.present();
        //then
        verify(command).execute();
        verify(presentation).showEndingContent();
    }

    @Test
    public void showContent_wrongInput_shouldExecute() throws InternalException {
        //given
        presentation.setScanner(new Scanner(TestUtils.buildInputStream("01-01-2019", "01/01/2019", "31/12/2019")));
        given(beanFactory.getBean(TransactionsCommand.class)).willReturn(command);
        given(command.execute()).willReturn(mockOutputDto());
        //when
        presentation.present();
        //then
        verify(command).execute();
        verify(presentation).showEndingContent();
    }

    @Test
    public void showContent_emptyResult_shouldExecute() throws InternalException {
        //given
        presentation.setScanner(new Scanner(TestUtils.buildInputStream("01/01/2019", "31/12/2019")));
        given(beanFactory.getBean(TransactionsCommand.class)).willReturn(command);
        given(command.execute()).willReturn(mockEmptyOutputDto());
        //when
        presentation.present();
        //then
        verify(command).execute();
        verify(presentation).showEndingContent();
    }

    @Test
    public void showContent_shouldHandleInternalException() throws InternalException {
        //given
        presentation.setScanner(new Scanner(TestUtils.buildInputStream("01/01/2019", "31/12/2019")));
        given(beanFactory.getBean(TransactionsCommand.class)).willReturn(command);
        given(command.execute()).willThrow(new InternalException("error happened"));
        //when
        presentation.present();
        //then
        verify(command).execute();
        verify(presentation).showEndingContent();
    }

    @Test
    public void showContent_shouldHandleCallingRestException() throws InternalException {
        //given
        presentation.setScanner(new Scanner(TestUtils.buildInputStream("01/01/2019", "31/12/2019")));
        given(beanFactory.getBean(TransactionsCommand.class)).willReturn(command);
        given(command.execute()).willThrow(TestUtils.mockCallingRestException());
        //when
        presentation.present();
        //then
        verify(command).execute();
        verify(presentation).showEndingContent();
    }

    private TransactionsOutputDto mockEmptyOutputDto() {
        TransactionsOutputDto output = new TransactionsOutputDto();
        output.setList(emptyList());
        return output;
    }

    private TransactionsOutputDto mockOutputDto() {
        TransactionsOutputDto output = new TransactionsOutputDto();
        TransactionApi transaction = new TransactionApi();
        transaction.setTransactionId("123456");
        transaction.setOperationId("456789");
        transaction.setAmount(BigDecimal.valueOf(123.45));
        transaction.setCurrency("EUR");
        transaction.setDescription("asdasd");
        transaction.setAccountingDate(LocalDate.of(2019,3,2));
        transaction.setValueDate(LocalDate.of(2019,5,4));
        output.setList(Collections.singletonList(transaction));
        return output;
    }
}