package com.siteminder.email.dispatcher.controller;

import com.siteminder.email.dispatcher.dto.EmailSendRequest;
import com.siteminder.email.dispatcher.dto.EmailSendResponse;
import com.siteminder.email.dispatcher.service.EmailDispatcher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(value = "email-dispatcher")
@RequestMapping("/api/v1")
public class EmailDispatcherController {

    private EmailDispatcher emailDispatcherService;

    public EmailDispatcherController(EmailDispatcher emailDispatcherService) {
        this.emailDispatcherService = emailDispatcherService;
    }

    @ApiOperation(value = "Sends the email using a email service provider", response = EmailSendResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully send the email"),
            @ApiResponse(code = 400, message = "Bad email send request"),
            @ApiResponse(code = 404, message = "The requested url is malformed")
    })
    @PostMapping("/email")
    public ResponseEntity<EmailSendResponse> sendEmail(@RequestBody @Valid EmailSendRequest emailSendRequest) {
        final EmailSendResponse sendResponse = emailDispatcherService.sendEmail(emailSendRequest);
        return sendResponse.isSuccess() ? ResponseEntity.ok(sendResponse) :
                ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(sendResponse);
    }
}
