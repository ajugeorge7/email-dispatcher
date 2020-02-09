package com.siteminder.email.dispatcher.service;

import com.siteminder.email.dispatcher.dto.EmailSendRequest;
import com.siteminder.email.dispatcher.dto.EmailSendResponse;
import com.siteminder.email.dispatcher.model.EmailProviderRequest;
import com.siteminder.email.dispatcher.model.EmailProviderResponse;
import com.siteminder.email.dispatcher.service.provider.EmailProviderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.siteminder.email.dispatcher.util.TestUtil.createEmailProviderRequest;
import static com.siteminder.email.dispatcher.util.TestUtil.createEmailSendRequest;
import static com.siteminder.email.dispatcher.util.TestUtil.createFailedEmailProviderResponse;
import static com.siteminder.email.dispatcher.util.TestUtil.createSuccessfulEmailProviderResponse;
import static com.siteminder.email.dispatcher.util.TestUtil.createSuccessfulEmailSendResponse;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailFailOverServiceTest {

    @Mock
    private EmailProviderService providerOneService;

    @Mock
    private EmailProviderService providerTwoService;

    private EmailFailOverService emailFailOverService;

    @Before
    public void setUp() {
        emailFailOverService = new EmailFailOverService(asList(providerOneService, providerTwoService));
    }

    @Test
    public void shouldReturnSuccessIfProviderOneReturnsSuccess() throws Exception {
        final EmailSendRequest emailSendRequest = createEmailSendRequest();
        final EmailProviderRequest emailProviderRequest = createEmailProviderRequest();
        final EmailProviderResponse emailProviderResponse = createSuccessfulEmailProviderResponse();

        when(providerOneService.sendEmail(emailProviderRequest)).thenReturn(emailProviderResponse);

        EmailSendResponse sendResponse = emailFailOverService.sendEmail(emailSendRequest);

        final EmailSendResponse expectedSendResponse = createSuccessfulEmailSendResponse();

        assertThat(sendResponse, is(expectedSendResponse));

        verifyNoInteractions(providerTwoService);
    }

    @Test
    public void shouldReturnSuccessIfProviderOneIsFailsAndProviderTwoIsSuccess() throws Exception {
        final EmailSendRequest emailSendRequest = createEmailSendRequest();
        final EmailProviderRequest emailProviderRequest = createEmailProviderRequest();
        final EmailProviderResponse providerOneResponse = createFailedEmailProviderResponse(401, "Failed to connect");
        final EmailProviderResponse providerTwoResponse = createSuccessfulEmailProviderResponse();

        when(providerOneService.sendEmail(emailProviderRequest)).thenReturn(providerOneResponse);
        when(providerTwoService.sendEmail(emailProviderRequest)).thenReturn(providerTwoResponse);

        EmailSendResponse sendResponse = emailFailOverService.sendEmail(emailSendRequest);

        final EmailSendResponse expectedSendResponse = EmailSendResponse.builder()
                .success(true)
                .errorMessages(singletonList("Unauthorized: Failed to connect"))
                .build();

        assertThat(sendResponse, is(expectedSendResponse));
    }

    @Test
    public void shouldReturnFailureIfProviderOneAndProviderTwoFails() throws Exception {
        final EmailSendRequest emailSendRequest = createEmailSendRequest();
        final EmailProviderRequest emailProviderRequest = createEmailProviderRequest();
        final EmailProviderResponse providerOneResponse = createFailedEmailProviderResponse(401, "Failed to connect");
        final EmailProviderResponse providerTwoResponse = createFailedEmailProviderResponse(403, "Not able to connect");

        when(providerOneService.sendEmail(emailProviderRequest)).thenReturn(providerOneResponse);
        when(providerTwoService.sendEmail(emailProviderRequest)).thenReturn(providerTwoResponse);

        EmailSendResponse sendResponse = emailFailOverService.sendEmail(emailSendRequest);

        final EmailSendResponse expectedSendResponse = EmailSendResponse.builder()
                .success(false)
                .errorMessages(asList("Unauthorized: Failed to connect", "Forbidden: Not able to connect"))
                .build();

        assertThat(sendResponse, is(expectedSendResponse));
    }

    @Test
    public void shouldReturnFailureWithMessageWithoutAnySpecificErrorMessage() throws Exception {
        final EmailSendRequest emailSendRequest = createEmailSendRequest();
        final EmailProviderRequest emailProviderRequest = createEmailProviderRequest();
        final EmailProviderResponse providerOneResponse = createFailedEmailProviderResponse(401, "");
        final EmailProviderResponse providerTwoResponse = createFailedEmailProviderResponse(403, "Not able to connect");

        when(providerOneService.sendEmail(emailProviderRequest)).thenReturn(providerOneResponse);
        when(providerTwoService.sendEmail(emailProviderRequest)).thenReturn(providerTwoResponse);

        EmailSendResponse sendResponse = emailFailOverService.sendEmail(emailSendRequest);

        final EmailSendResponse expectedSendResponse = EmailSendResponse.builder()
                .success(false)
                .errorMessages(asList("Unauthorized", "Forbidden: Not able to connect"))
                .build();

        assertThat(sendResponse, is(expectedSendResponse));
    }
}