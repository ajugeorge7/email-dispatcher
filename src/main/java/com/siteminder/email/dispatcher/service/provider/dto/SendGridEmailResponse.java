package com.siteminder.email.dispatcher.service.provider.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendGridEmailResponse {

    private String id;
    private String message;
}
