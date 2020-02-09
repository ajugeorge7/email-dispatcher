package com.siteminder.email.dispatcher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static java.util.Collections.emptyList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailSendRequest {

    @NotBlank
    private String from;

    @NotEmpty
    private List<String> toList;

    private List<String> ccList = emptyList();

    private List<String> bccList = emptyList();

    @NotBlank
    private String subject;

    @NotBlank
    private String body;
}
