package com.siteminder.email.dispatcher.service;

import com.siteminder.email.dispatcher.dto.EmailSendRequest;
import com.siteminder.email.dispatcher.dto.EmailSendResponse;
import com.siteminder.email.dispatcher.exception.EmailDispatchException;
import com.siteminder.email.dispatcher.model.EmailProviderRequest;
import com.siteminder.email.dispatcher.model.EmailProviderResponse;
import com.siteminder.email.dispatcher.service.provider.EmailProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.siteminder.email.dispatcher.helper.StatusCodeMapper.getMessage;
import static java.lang.String.format;

@Service
public class EmailFailOverService implements EmailDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailFailOverService.class);

    private List<EmailProviderService> providerServices;

    public EmailFailOverService(List<EmailProviderService> providerServices) {
        this.providerServices = providerServices;
    }

    @Override
    public EmailSendResponse sendEmail(EmailSendRequest emailSendRequest) {
        final List<String> errorMessages = new ArrayList<>();
        final EmailProviderRequest providerRequest = EmailProviderRequest.builder()
                .from(emailSendRequest.getFrom())
                .toList(emailSendRequest.getToList())
                .ccList(emailSendRequest.getCcList())
                .bccList(emailSendRequest.getBccList())
                .subject(emailSendRequest.getSubject())
                .body(emailSendRequest.getBody())
                .build();

        boolean sendEmail = attemptSend(providerRequest, errorMessages);

        return EmailSendResponse.builder()
                .success(sendEmail)
                .errorMessages(errorMessages)
                .build();
    }

    private boolean attemptSend(EmailProviderRequest providerRequest, List<String> errorMessages) {
        boolean sendEmail = false;
        for (EmailProviderService providerService : providerServices) {
            LOGGER.info("Attempting to send email with {}", providerService.getClass().getSimpleName());
            String errorMessage = attemptSendWith(providerService, providerRequest);
            if (errorMessage.isEmpty()) {
                LOGGER.info("Successful in sending email with {}", providerService.getClass().getSimpleName());
                sendEmail = true;
                break;
            }
            errorMessages.add(errorMessage);
            LOGGER.warn("Failed to send email with {}", providerService.getClass().getSimpleName());
        }

        return sendEmail;
    }

    private String attemptSendWith(EmailProviderService providerService,
                                   EmailProviderRequest providerRequest) {
        try {
            final EmailProviderResponse providerResponse = providerService.sendEmail(providerRequest);
            return formattedErrorMessage(providerResponse);
        } catch (EmailDispatchException ex) {
            return ex.getMessage();
        }
    }

    private String formattedErrorMessage(EmailProviderResponse providerResponse) {
        final String errorMessage = getMessage(providerResponse.getStatusCode());

        if (!errorMessage.isEmpty() && !providerResponse.getBody().isEmpty()) {
            return format("%s: %s", errorMessage, providerResponse.getBody());
        }

        return errorMessage;
    }
}
