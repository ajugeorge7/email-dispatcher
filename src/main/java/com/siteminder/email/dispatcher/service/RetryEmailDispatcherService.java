package com.siteminder.email.dispatcher.service;

import com.siteminder.email.dispatcher.dto.EmailSendRequest;
import com.siteminder.email.dispatcher.dto.EmailSendResponse;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

public class RetryEmailDispatcherService implements EmailDispatcher {

    @Value("${email.send.retry.count}")
    private Integer retryCount;

    private EmailDispatcher emailDispatcher;

    public RetryEmailDispatcherService(EmailDispatcher emailDispatcher) {
        this.emailDispatcher = emailDispatcher;
    }

    @Override
    public EmailSendResponse sendEmail(EmailSendRequest emailSendRequest) {
        boolean success = false;
        final List<String> errorMessages = new ArrayList<>();
        for (int count = 0; !success && count < retryCount; ++count) {
            EmailSendResponse emailSendResponse = emailDispatcher.sendEmail(emailSendRequest);
            errorMessages.addAll(emailSendResponse.getErrorMessages());
            if (emailSendResponse.isSuccess()) {
                success = true;
            }
        }

        return EmailSendResponse.builder()
                .success(success)
                .errorMessages(errorMessages)
                .build();
    }
}
