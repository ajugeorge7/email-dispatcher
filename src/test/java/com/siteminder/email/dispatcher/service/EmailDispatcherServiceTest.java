package com.siteminder.email.dispatcher.service;

import com.siteminder.email.dispatcher.dto.EmailSendRequest;
import com.siteminder.email.dispatcher.dto.EmailSendResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.siteminder.email.dispatcher.util.TestUtil.createEmailSendRequest;
import static com.siteminder.email.dispatcher.util.TestUtil.createFailedEmailSendResponse;
import static com.siteminder.email.dispatcher.util.TestUtil.createSuccessfulEmailSendResponse;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailDispatcherServiceTest {

    @Mock
    private EmailFailOverService emailFailOverService;

    private EmailDispatcherService emailDispatcherService;

    @Before
    public void setUp() {
        emailDispatcherService = new EmailDispatcherService(emailFailOverService);
    }

    @Test
    public void shouldReturnSuccessfulResponseInvokingTheFailOverServiceIsSuccess() {

        final EmailSendRequest emailSendRequest = createEmailSendRequest();
        final EmailSendResponse emailSendResponse = createSuccessfulEmailSendResponse();

        when(emailFailOverService.sendEmail(emailSendRequest)).thenReturn(emailSendResponse);

        EmailSendResponse actualEmailSendResponse = emailDispatcherService.sendEmail(emailSendRequest);

        assertThat(actualEmailSendResponse, is(emailSendResponse));
    }

    @Test
    public void shouldReturnUnSuccessfulResponseInvokingTheFailOverServiceisFailed() {

        final EmailSendRequest emailSendRequest = createEmailSendRequest();
        final EmailSendResponse emailSendResponse = createFailedEmailSendResponse();

        when(emailFailOverService.sendEmail(emailSendRequest)).thenReturn(emailSendResponse);

        EmailSendResponse actualEmailSendResponse = emailDispatcherService.sendEmail(emailSendRequest);

        assertThat(actualEmailSendResponse, is(emailSendResponse));
    }
}