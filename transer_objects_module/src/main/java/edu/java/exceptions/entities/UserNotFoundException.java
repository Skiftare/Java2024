package edu.java.exceptions.entities;

import edu.java.exceptions.RequestProcessingException;

public class UserNotFoundException extends RequestProcessingException {
    public UserNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Chat id not found";
    }
}
