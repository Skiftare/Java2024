package edu.java.exceptions.entities;

import edu.java.exceptions.RequestProcessingException;

public class LinkAlreadyExistException extends RequestProcessingException {
    public LinkAlreadyExistException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Link already exist";
    }
}
