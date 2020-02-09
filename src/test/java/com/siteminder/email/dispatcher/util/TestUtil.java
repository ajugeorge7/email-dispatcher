package com.siteminder.email.dispatcher.util;

import com.siteminder.email.dispatcher.dto.EmailSendRequest;
import com.siteminder.email.dispatcher.dto.EmailSendResponse;
import com.siteminder.email.dispatcher.model.EmailProviderRequest;
import com.siteminder.email.dispatcher.model.EmailProviderResponse;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class TestUtil {

    private TestUtil() {
    }

    public static EmailSendRequest createEmailSendRequest() {
        return EmailSendRequest.builder()
                .from("customer.one@gmail.com")
                .toList(asList("hotel.manager@gmail.com", "hotel.staff@gmail.com"))
                .subject("Booking Email")
                .body("Booking Email")
                .build();
    }

    public static EmailProviderRequest createEmailProviderRequest() {
        return EmailProviderRequest.builder()
                .from("customer.one@gmail.com")
                .toList(asList("hotel.manager@gmail.com", "hotel.staff@gmail.com"))
                .subject("Booking Email")
                .body("Booking Email")
                .build();
    }

    public static EmailSendResponse createSuccessfulEmailSendResponse() {
        return EmailSendResponse.builder()
                .success(true)
                .errorMessages(emptyList())
                .build();
    }

    public static EmailSendResponse createFailedEmailSendResponse() {
        return EmailSendResponse.builder()
                .success(false)
                .errorMessages(asList("Provider One failed", "Provider Two failed"))
                .build();
    }

    public static EmailProviderResponse createSuccessfulEmailProviderResponse() {
        return EmailProviderResponse.builder()
                .statusCode(200)
                .build();
    }

    public static EmailProviderResponse createSuccessfulEmailProviderResponseWithBody(String body) {
        return EmailProviderResponse.builder()
                .statusCode(200)
                .body(body)
                .build();
    }

    public static EmailProviderResponse createFailedEmailProviderResponse() {
        return EmailProviderResponse.builder()
                .statusCode(401)
                .body("Unauthorised")
                .build();
    }

    public static EmailProviderResponse createFailedEmailProviderResponse(int statusCode, String body) {
        return EmailProviderResponse.builder()
                .statusCode(statusCode)
                .body(body)
                .build();
    }
}
