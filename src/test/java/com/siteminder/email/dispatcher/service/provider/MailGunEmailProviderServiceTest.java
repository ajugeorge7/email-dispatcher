package com.siteminder.email.dispatcher.service.provider;

import com.siteminder.email.dispatcher.exception.EmailDispatchException;
import com.siteminder.email.dispatcher.model.EmailProviderRequest;
import com.siteminder.email.dispatcher.model.EmailProviderResponse;
import com.siteminder.email.dispatcher.service.provider.dto.MailGunEmailResponse;
import com.siteminder.email.dispatcher.service.rest.RestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import static com.siteminder.email.dispatcher.util.TestUtil.createEmailProviderRequest;
import static com.siteminder.email.dispatcher.util.TestUtil.createFailedEmailProviderResponse;
import static com.siteminder.email.dispatcher.util.TestUtil.createSuccessfulEmailProviderResponse;
import static com.siteminder.email.dispatcher.util.TestUtil.createSuccessfulEmailProviderResponseWithBody;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MailGunEmailProviderServiceTest {

    @Mock
    private RestService<MailGunEmailResponse> restService;

    private MailGunEmailProviderService mailGunEmailProviderService;

    @Before
    public void setUp() {
        mailGunEmailProviderService = new MailGunEmailProviderService(restService);
    }

    @Test
    public void shouldReturnSuccessfulResponseForValidResponseFromMainGunServer() throws Exception {
        final EmailProviderRequest emailProviderRequest = createEmailProviderRequest();
        final MailGunEmailResponse mailGunEmailResponse = new MailGunEmailResponse();
        final ResponseEntity<MailGunEmailResponse> responseEntity = new ResponseEntity<>(mailGunEmailResponse, HttpStatus.OK);

        when(restService.exchange(any(), any(), any(), any())).thenReturn(responseEntity);

        EmailProviderResponse providerResponse = mailGunEmailProviderService.sendEmail(emailProviderRequest);

        final EmailProviderResponse expectedProviderResponse = createSuccessfulEmailProviderResponse();

        assertThat(providerResponse, is(expectedProviderResponse));
    }

    @Test
    public void shouldReturnSuccessfulResponseWithMessageForValidResponseFromMainGunServer() throws Exception {
        final EmailProviderRequest emailProviderRequest = createEmailProviderRequest();
        final MailGunEmailResponse mailGunEmailResponse = MailGunEmailResponse.builder()
                .message("Message Queued")
                .build();
        final ResponseEntity<MailGunEmailResponse> responseEntity = new ResponseEntity<>(mailGunEmailResponse, HttpStatus.OK);

        when(restService.exchange(any(), any(), any(), any())).thenReturn(responseEntity);

        EmailProviderResponse providerResponse = mailGunEmailProviderService.sendEmail(emailProviderRequest);

        final EmailProviderResponse expectedProviderResponse = createSuccessfulEmailProviderResponseWithBody("Message Queued");

        assertThat(providerResponse, is(expectedProviderResponse));
    }

    @Test
    public void shouldReturnResponseWithErrorStatusForInValidResponseFromMainGunServer() throws Exception {
        final EmailProviderRequest emailProviderRequest = createEmailProviderRequest();
        final MailGunEmailResponse mailGunEmailResponse = MailGunEmailResponse.builder()
                .message("Failed to connect")
                .build();
        final ResponseEntity<MailGunEmailResponse> responseEntity = new ResponseEntity<>(mailGunEmailResponse, HttpStatus.UNAUTHORIZED);

        when(restService.exchange(any(), any(), any(), any())).thenReturn(responseEntity);

        EmailProviderResponse providerResponse = mailGunEmailProviderService.sendEmail(emailProviderRequest);

        final EmailProviderResponse expectedProviderResponse = createFailedEmailProviderResponse(401, "Failed to connect");

        assertThat(providerResponse, is(expectedProviderResponse));
    }

    @Test(expected = EmailDispatchException.class)
    public void shouldThrowExceptionForRestClientExceptionFromMainGunServer() throws Exception {
        final EmailProviderRequest emailProviderRequest = createEmailProviderRequest();

        when(restService.exchange(any(), any(), any(), any())).thenThrow(new RestClientException("Client Error"));

        mailGunEmailProviderService.sendEmail(emailProviderRequest);
    }
}