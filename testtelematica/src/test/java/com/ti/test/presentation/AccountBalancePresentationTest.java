package com.ti.test.presentation;

import com.ti.test.TestUtils;
import com.ti.test.command.AccountBalanceCommand;
import com.ti.test.exception.InternalException;
import com.ti.test.model.dto.AccountOutputDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.BeanFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AccountBalancePresentationTest {

    @InjectMocks
    @Spy
    private AccountBalancePresentation presentation;
    @Mock
    private BeanFactory beanFactory;
    @Mock
    private AccountBalanceCommand command;

    @Test
    public void showContent_shouldExecute() throws InternalException {
        //given
        given(beanFactory.getBean(AccountBalanceCommand.class)).willReturn(command);
        given(command.execute()).willReturn(mockOutputDto());
        //when
        presentation.present();
        //then
        verify(command).execute();
        verify(presentation).showEndingContent();
    }

    @Test
    public void showContent_shouldHandleInternalException() throws InternalException {
        //given
        given(beanFactory.getBean(AccountBalanceCommand.class)).willReturn(command);
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
        given(beanFactory.getBean(AccountBalanceCommand.class)).willReturn(command);
        given(command.execute()).willThrow(TestUtils.mockCallingRestException());
        //when
        presentation.present();
        //then
        verify(command).execute();
        verify(presentation).showEndingContent();
    }

    private AccountOutputDto mockOutputDto() {
        AccountOutputDto out = new AccountOutputDto();
        out.setBalance(BigDecimal.ONE);
        out.setAvailableBalance(BigDecimal.TEN);
        out.setDate(LocalDate.now());
        out.setCurrency("EUR");
        return out;
    }
}