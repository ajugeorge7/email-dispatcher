package com.siteminder.email.dispatcher.service.provider;

import com.siteminder.email.dispatcher.exception.EmailDispatchException;
import com.siteminder.email.dispatcher.model.EmailProviderRequest;
import com.siteminder.email.dispatcher.model.EmailProviderResponse;
import com.siteminder.email.dispatcher.service.provider.dto.MailGunEmailResponse;
import com.siteminder.email.dispatcher.service.rest.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service("primary")
public class MailGunEmailProviderService implements EmailProviderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailGunEmailProviderService.class);

    private static final String FROM = "from";
    private static final String TO = "to";
    private static final String CC = "cc";
    private static final String BCC = "bcc";
    private static final String SUBJECT = "subject";
    private static final String TEXT = "text";

    @Value("${email.mailGun.api.url}")
    private String mailGunApiUrl;

    @Value("${email.mailGun.api.user}")
    private String mailGunApiUser;

    @Value("${email.mailGun.api.password}")
    private String mailGunApiPassword;

    private RestService<MailGunEmailResponse> restService;

    public MailGunEmailProviderService(RestService<MailGunEmailResponse> restService) {
        this.restService = restService;
    }

    @Override
    public EmailProviderResponse sendEmail(EmailProviderRequest providerRequest) throws EmailDispatchException {
        final MultiValueMap<String, String> params = getFormParams(providerRequest);
        final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, createHeaders());

        try {
            final ResponseEntity<MailGunEmailResponse> response = restService.exchange(mailGunApiUrl,
                    HttpMethod.POST,requestEntity, MailGunEmailResponse.class);

            return EmailProviderResponse.builder()
                    .statusCode(response.getStatusCode().value())
                    .body(getBody(response))
                    .build();
        } catch (RestClientException ex) {
            final String errorMessage = format("Failed to send email using MailGun due to %s", ex.getMessage());
            LOGGER.error(errorMessage, ex);
            throw new EmailDispatchException(errorMessage);
        }
    }

    private MultiValueMap<String, String> getFormParams(EmailProviderRequest providerRequest) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(FROM, providerRequest.getFrom());
        params.add(TO, getCommaSeparatedString(providerRequest.getToList()));
        if (!CollectionUtils.isEmpty(providerRequest.getCcList())) {
            params.add(CC, getCommaSeparatedString(providerRequest.getCcList()));
        }
        if (!CollectionUtils.isEmpty(providerRequest.getBccList())) {
            params.add(BCC, getCommaSeparatedString(providerRequest.getBccList()));
        }
        params.add(SUBJECT, providerRequest.getSubject());
        params.add(TEXT, providerRequest.getBody());
        return params;
    }

    private HttpHeaders createHeaders() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(mailGunApiUser, mailGunApiPassword);
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        return httpHeaders;
    }

    private String getCommaSeparatedString(List<String> stringList) {
        return stringList.stream().collect(Collectors.joining(","));
    }

    private String getBody(ResponseEntity<MailGunEmailResponse> response) {
        if (response.getBody() != null) {
            return response.getBody().getMessage();
        }

        return "";
    }
}