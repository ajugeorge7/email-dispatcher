package com.siteminder.email.dispatcher.service.provider.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class SendGridEmailRequest {

    @JsonProperty("from")
    public Email from;

    @JsonProperty("subject")
    public String subject;

    @JsonProperty("personalizations")
    public List<Personalization> personalization;

    @JsonProperty("content")
    public List<Content> content;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(NON_DEFAULT)
    public static class Email {

        @JsonProperty("name")
        private String name;

        @JsonProperty("email")
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(NON_DEFAULT)
    public static class Personalization {

        @JsonProperty("to")
        private List<Email> tos;

        @JsonProperty("cc")
        private List<Email> ccs;

        @JsonProperty("bcc")
        private List<Email> bccs;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(NON_DEFAULT)
    public static class Content {

        @JsonProperty("type")
        private String type;

        @JsonProperty("value")
        private String value;
    }
}


