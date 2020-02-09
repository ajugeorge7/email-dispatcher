package com.siteminder.email.dispatcher.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServerMessage {

    private final Date timestamp = new Date();
    private final boolean error;
    private final List<String> messages = new ArrayList<>();

    public ServerMessage(boolean error, List<String> messages) {
        this.error = error;
        this.messages.addAll(messages);
    }

    public boolean isError() {
        return error;
    }

    public List<String> getMessages() {
        return messages;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
