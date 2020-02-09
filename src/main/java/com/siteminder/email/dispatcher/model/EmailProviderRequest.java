package com.siteminder.email.dispatcher.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class EmailProviderRequest {

    private String from;
    private List<String> toList;
    private List<String> ccList;
    private List<String> bccList;
    private String subject;
    private String body;
}
