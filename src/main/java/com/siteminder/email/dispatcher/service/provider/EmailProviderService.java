package com.siteminder.email.dispatcher.service.provider;

import com.siteminder.email.dispatcher.exception.EmailDispatchException;
import com.siteminder.email.dispatcher.model.EmailProviderRequest;
import com.siteminder.email.dispatcher.model.EmailProviderResponse;

public interface EmailProviderService {

    EmailProviderResponse sendEmail(EmailProviderRequest sendRequest) throws EmailDispatchException;
}
