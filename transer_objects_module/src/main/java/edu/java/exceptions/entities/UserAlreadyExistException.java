package edu.java.exceptions.entities;

import edu.java.exceptions.RequestProcessingException;

public class UserAlreadyExistException extends RequestProcessingException {
    public UserAlreadyExistException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "User already exist";
    }
}
