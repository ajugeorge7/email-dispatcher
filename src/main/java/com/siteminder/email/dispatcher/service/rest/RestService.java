package com.siteminder.email.dispatcher.service.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService<Response> {

    private RestTemplate restTemplate;

    public RestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Response> exchange(String url, HttpMethod httpMethod,
                                             HttpEntity requestEntity, Class<Response> responseClass) {

        return restTemplate.exchange(url, httpMethod, requestEntity, responseClass);
    }
}
