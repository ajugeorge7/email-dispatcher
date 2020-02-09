package com.siteminder.email.dispatcher.helper;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class StatusCodeMapperTest {

    @Test
    public void shouldReturnAppropriateMessageFor400() {
        assertThat(StatusCodeMapper.getMessage(400), is("Bad request"));
    }

    @Test
    public void shouldReturnAppropriateMessageFor401() {
        assertThat(StatusCodeMapper.getMessage(401), is("Unauthorized"));
    }

    @Test
    public void shouldReturnAppropriateMessageFor403() {
        assertThat(StatusCodeMapper.getMessage(403), is("Forbidden"));
    }

    @Test
    public void shouldReturnAppropriateMessageFor404() {
        assertThat(StatusCodeMapper.getMessage(404), is("Not Found"));
    }

    @Test
    public void shouldReturnAppropriateMessageFor500() {
        assertThat(StatusCodeMapper.getMessage(500), is("Server Error"));
    }

    @Test
    public void shouldReturnAppropriateMessageFor503() {
        assertThat(StatusCodeMapper.getMessage(503), is("Service Not Available"));
    }

}