package com.siteminder.email.dispatcher.service;

import com.siteminder.email.dispatcher.dto.EmailSendRequest;
import com.siteminder.email.dispatcher.dto.EmailSendResponse;

public interface EmailDispatcher {

    EmailSendResponse sendEmail(EmailSendRequest emailSendRequest);
}
