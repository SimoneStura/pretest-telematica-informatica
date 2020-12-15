package com.ti.test.presentation;

import com.ti.test.TestUtils;
import com.ti.test.command.BankTransferCommand;
import com.ti.test.exception.InternalException;
import com.ti.test.model.dto.BankTransferOutputDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.BeanFactory;

import java.util.Scanner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class BankTransferPresentationTest {

    @InjectMocks
    @Spy
    private BankTransferPresentation presentation;
    @Mock
    private BeanFactory beanFactory;
    @Mock
    private BankTransferCommand command;

    @Before
    public void init() {
    }

    @Test
    public void showContent_shouldExecute() throws InternalException {
        //given
        presentation.setScanner(new Scanner(TestUtils.buildInputStream("123.45", "Pagamento Rata")));
        given(beanFactory.getBean(BankTransferCommand.class)).willReturn(command);
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
        presentation.setScanner(new Scanner(TestUtils.buildInputStream("123abc", "123.45", "     ", "Pagamento Rata")));
        given(beanFactory.getBean(BankTransferCommand.class)).willReturn(command);
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
        presentation.setScanner(new Scanner(TestUtils.buildInputStream("123.45", "Pagamento Rata")));
        given(beanFactory.getBean(BankTransferCommand.class)).willReturn(command);
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
        presentation.setScanner(new Scanner(TestUtils.buildInputStream("123.45", "Pagamento Rata")));
        given(beanFactory.getBean(BankTransferCommand.class)).willReturn(command);
        given(command.execute()).willThrow(TestUtils.mockCallingRestException());
        //when
        presentation.present();
        //then
        verify(command).execute();
        verify(presentation).showEndingContent();
    }

    private BankTransferOutputDto mockOutputDto() {
        BankTransferOutputDto output = new BankTransferOutputDto();
        output.setTransferSuccessful(true);
        return output;
    }
}