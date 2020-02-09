package com.siteminder.email.dispatcher.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EmailProviderResponse {

    private int statusCode;
    private String body;
}
