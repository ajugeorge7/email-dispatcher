package com.siteminder.email.dispatcher.service;

import com.siteminder.email.dispatcher.dto.EmailSendRequest;
import com.siteminder.email.dispatcher.dto.EmailSendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("emailDispatcherService")
public class EmailDispatcherService implements EmailDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailDispatcherService.class);

    private EmailFailOverService emailFailOverService;

    public EmailDispatcherService(EmailFailOverService emailFailOverService) {
        this.emailFailOverService = emailFailOverService;
    }

    public EmailSendResponse sendEmail(EmailSendRequest emailSendRequest) {
        LOGGER.info("Started sending email to all recipients");

        final EmailSendResponse emailSendResponse = emailFailOverService.sendEmail(emailSendRequest);

        LOGGER.info("Finished sending email to all recipients");

        return emailSendResponse;
    }
}
