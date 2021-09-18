package com.exception;

public class SocialNetworkNotFoundException extends Exception {

    String message;

    public SocialNetworkNotFoundException(String s) {
        message = s;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
