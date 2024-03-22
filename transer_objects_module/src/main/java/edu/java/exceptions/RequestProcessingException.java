package edu.java.exceptions;

//TODO: точно ли uncheked нужен?
public abstract class RequestProcessingException extends RuntimeException {
    public abstract String getMessage();

    public RequestProcessingException(String message) {
        super(message);
    }
}

