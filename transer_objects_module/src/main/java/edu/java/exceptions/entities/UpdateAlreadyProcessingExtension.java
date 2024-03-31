package edu.java.exceptions.entities;

public class UpdateAlreadyProcessingExtension extends RuntimeException {
    public UpdateAlreadyProcessingExtension(String message) {
        super(message);
    }

    public UpdateAlreadyProcessingExtension(String message, Throwable cause) {
        super(message, cause);
    }
}
