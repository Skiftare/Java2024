package edu.java.data.response;

public record TgChatInteractionResponse(
    long chatId,
    String message
) {
}
