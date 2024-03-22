package edu.java.exceptions.entities;

import edu.java.exceptions.RequestProcessingException;

public class LinkNotFoundException extends RequestProcessingException {
    public LinkNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Link not found";
    }
}
