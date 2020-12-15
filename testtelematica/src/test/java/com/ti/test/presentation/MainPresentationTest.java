package com.ti.test.presentation;

import com.ti.test.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.BeanFactory;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.noMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class MainPresentationTest {

    @InjectMocks
    private MainPresentation mainPresentation;
    @Mock
    private BeanFactory beanFactory;
    @Mock
    private AccountBalancePresentation accountBalancePresentation;
    @Mock
    private TransactionsPresentation transactionsPresentation;
    @Mock
    private BankTransferPresentation bankTransferPresentation;

    @Test
    public void present_wrongInput_shouldExecuteNoOperationAndExit() {
        //given
        TestUtils.mockConsoleInput("a", "b", "Q");
        //when
        mainPresentation.present();
        //then
        verify(accountBalancePresentation, noMoreInteractions()).present();
        verify(transactionsPresentation, noMoreInteractions()).present();
        verify(bankTransferPresentation, noMoreInteractions()).present();
    }

    @Test
    public void present_shouldExecuteAccountOpAndExit() {
        //given
        TestUtils.mockConsoleInput("1", "Q");
        given(beanFactory.getBean(AccountBalancePresentation.class)).willReturn(accountBalancePresentation);
        //when
        mainPresentation.present();
        //then
        verify(accountBalancePresentation).present();
        verify(transactionsPresentation, noMoreInteractions()).present();
        verify(bankTransferPresentation, noMoreInteractions()).present();
    }

    @Test
    public void present_shouldExecuteBankTransferOpAndExit() {
        //given
        TestUtils.mockConsoleInput("2", "Q");
        given(beanFactory.getBean(TransactionsPresentation.class)).willReturn(transactionsPresentation);
        //when
        mainPresentation.present();
        //then
        verify(accountBalancePresentation, noMoreInteractions()).present();
        verify(transactionsPresentation).present();
        verify(bankTransferPresentation, noMoreInteractions()).present();
    }

    @Test
    public void present_shouldExecuteTransactionsOpAndExit() {
        //given
        TestUtils.mockConsoleInput("3", "Q");
        given(beanFactory.getBean(BankTransferPresentation.class)).willReturn(bankTransferPresentation);
        //when
        mainPresentation.present();
        //then
        verify(accountBalancePresentation, noMoreInteractions()).present();
        verify(transactionsPresentation, noMoreInteractions()).present();
        verify(bankTransferPresentation).present();
    }

    @Test
    public void present_shouldExecuteTwoOperationsAndExit() {
        //given
        TestUtils.mockConsoleInput("1", "2", "Q");
        given(beanFactory.getBean(AccountBalancePresentation.class)).willReturn(accountBalancePresentation);
        given(beanFactory.getBean(TransactionsPresentation.class)).willReturn(transactionsPresentation);
        //when
        mainPresentation.present();
        //then
        verify(accountBalancePresentation).present();
        verify(transactionsPresentation).present();
        verify(bankTransferPresentation, noMoreInteractions()).present();
    }
}