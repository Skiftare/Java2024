package edu.java.api.web;

public record ResultOfServiceOperation(
    Long chatId,
    boolean wasSuccessful,
    String message
) {
}
