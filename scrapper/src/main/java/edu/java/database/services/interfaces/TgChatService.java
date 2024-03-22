package edu.java.database.services.interfaces;

public interface TgChatService {
    void register(long tgChatId);

    void unregister(long tgChatId);

    default String generateExceptionMessage(long id) {
        return String.format("tg chat id = %d", id);
    }
}
