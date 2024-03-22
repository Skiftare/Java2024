package edu.java.api.entities.responses;

public record TgChatInteractionResponse(
    long chatId,
    String message
) {
}
