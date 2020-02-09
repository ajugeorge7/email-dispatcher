package com.siteminder.email.dispatcher.service.provider;

import com.siteminder.email.dispatcher.exception.EmailDispatchException;
import com.siteminder.email.dispatcher.model.EmailProviderRequest;
import com.siteminder.email.dispatcher.model.EmailProviderResponse;
import com.siteminder.email.dispatcher.service.provider.dto.SendGridEmailRequest;
import com.siteminder.email.dispatcher.service.provider.dto.SendGridEmailResponse;
import com.siteminder.email.dispatcher.service.rest.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Collections.singletonList;

@Service("secondary")
public class SendGridEmailProviderService implements EmailProviderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendGridEmailProviderService.class);

    @Value("${email.sendGrid.api.url}")
    private String sendGridApiUrl;

    @Value("${email.sendGrid.api.key}")
    private String sendGridApiKey;

    private RestService<SendGridEmailResponse> restService;

    public SendGridEmailProviderService(RestService<SendGridEmailResponse> restService) {
        this.restService = restService;
    }

    @Override
    public EmailProviderResponse sendEmail(EmailProviderRequest providerRequest) throws EmailDispatchException {
        final SendGridEmailRequest sendGridEmailRequest = createMail(providerRequest);
        final HttpEntity<SendGridEmailRequest> requestEntity = new HttpEntity<>(sendGridEmailRequest, createHeaders());

        try {
            final ResponseEntity<SendGridEmailResponse> response = restService.exchange(sendGridApiUrl,
                    HttpMethod.POST, requestEntity, SendGridEmailResponse.class);

            return EmailProviderResponse.builder()
                    .statusCode(response.getStatusCode().value())
                    .body(getBody(response))
                    .build();
        } catch (RestClientException ex) {
            final String errorMessage = format("Failed to send email using SendGrid due to %s", ex.getMessage());
            LOGGER.error(errorMessage, ex);
            throw new EmailDispatchException(errorMessage);
        }
    }

    private SendGridEmailRequest createMail(EmailProviderRequest providerRequest) {
        SendGridEmailRequest.Personalization.PersonalizationBuilder personalizationBuilder = SendGridEmailRequest.Personalization.builder()
                .tos(getEmailList(providerRequest.getToList()));
        if (!CollectionUtils.isEmpty(providerRequest.getCcList())) {
            personalizationBuilder.ccs(getEmailList(providerRequest.getCcList()));
        }
        if (!CollectionUtils.isEmpty(providerRequest.getBccList())) {
            personalizationBuilder.bccs(getEmailList(providerRequest.getBccList()));
        }
        final SendGridEmailRequest.Content content = SendGridEmailRequest.Content.builder()
                .type(MediaType.TEXT_PLAIN_VALUE)
                .value(providerRequest.getBody())
                .build();

        return SendGridEmailRequest.builder()
                .from(SendGridEmailRequest.Email.builder().email(providerRequest.getFrom()).build())
                .subject(providerRequest.getSubject())
                .content(singletonList(content))
                .personalization(singletonList(personalizationBuilder.build()))
                .build();
    }

    private HttpHeaders createHeaders() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(sendGridApiKey);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    private List<SendGridEmailRequest.Email> getEmailList(List<String> emails) {
        return emails.stream()
                .map(it -> SendGridEmailRequest.Email.builder().email(it).build())
                .collect(Collectors.toList());
    }

    private String getBody(ResponseEntity<SendGridEmailResponse> response) {
        if (response.getBody() != null) {
            return response.getBody().getMessage();
        }

        return "";
    }
}
