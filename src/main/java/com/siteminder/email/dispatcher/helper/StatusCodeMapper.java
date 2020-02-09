package com.siteminder.email.dispatcher.helper;

public class StatusCodeMapper {

    private StatusCodeMapper() {
    }

    public static String getMessage(int statusCode) {
        switch (statusCode) {
            case 400: return "Bad request";
            case 401: return "Unauthorized";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 500: return "Server Error";
            case 503: return "Service Not Available";
            default: return "";
        }
    }
}
