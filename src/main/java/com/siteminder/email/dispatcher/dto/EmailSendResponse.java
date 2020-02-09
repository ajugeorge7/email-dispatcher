package com.siteminder.email.dispatcher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailSendResponse {

    private boolean success;
    private List<String> errorMessages;
}
