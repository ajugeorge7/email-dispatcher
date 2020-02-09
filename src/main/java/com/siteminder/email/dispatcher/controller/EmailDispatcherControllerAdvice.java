package com.siteminder.email.dispatcher.controller;

import com.siteminder.email.dispatcher.dto.ServerMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class EmailDispatcherControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(EmailDispatcherControllerAdvice::toValidationErrorMsg)
                .sorted()
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ServerMessage(true, errors), HttpStatus.BAD_REQUEST);
    }

    private static String toValidationErrorMsg(FieldError fieldError) {
        return fieldError.getObjectName() + '.' + fieldError.getField() + " " + fieldError.getDefaultMessage();
    }
}
