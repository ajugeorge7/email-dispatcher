package com.siteminder.email.dispatcher.controller;

import com.google.gson.Gson;
import com.siteminder.email.dispatcher.dto.EmailSendRequest;
import com.siteminder.email.dispatcher.dto.EmailSendResponse;
import com.siteminder.email.dispatcher.service.EmailDispatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.siteminder.email.dispatcher.util.TestUtil.createEmailSendRequest;
import static com.siteminder.email.dispatcher.util.TestUtil.createFailedEmailSendResponse;
import static com.siteminder.email.dispatcher.util.TestUtil.createSuccessfulEmailSendResponse;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EmailDispatcherController.class)
public class EmailDispatcherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private EmailDispatcher emailDispatcher;

    @Test
    public void shouldReturnBadRequestIfTheRequestIsMissingToList() throws Exception {

        final EmailSendRequest sendRequest = EmailSendRequest.builder()
                .from("customer.one@gmail.com")
                .subject("Booking Email")
                .body("Booking Email")
                .build();

        mockMvc.perform(post("/api/v1/email")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(sendRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messages[0]", containsString("toList must not be empty")));
    }

    @Test
    public void shouldReturnSuccessResponseIfTheEmailDispatchIsSuccessful() throws Exception {

        final EmailSendRequest sendRequest = createEmailSendRequest();
        final EmailSendResponse sendResponse = createSuccessfulEmailSendResponse();

        when(emailDispatcher.sendEmail(any())).thenReturn(sendResponse);

        mockMvc.perform(post("/api/v1/email")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(sendRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(sendResponse)));
    }

    @Test
    public void shouldReturnErrorResponseIfTheEmailDispatchIsUnSuccessful() throws Exception {

        final EmailSendRequest sendRequest = createEmailSendRequest();
        final EmailSendResponse sendResponse = createFailedEmailSendResponse();

        when(emailDispatcher.sendEmail(any())).thenReturn(sendResponse);

        mockMvc.perform(post("/api/v1/email")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(sendRequest)))
                .andExpect(status().isExpectationFailed())
                .andExpect(content().json(gson.toJson(sendResponse)));
    }
}