package edu.java.database.services.interfaces;

public interface TgChatService {

    void register(long tgChatId);

    void unregister(long tgChatId);

    boolean isRegistered(long tgChatId);

    default String generateExceptionMessageForChatId(long id) {
        return String.format("tg chat id = %d", id);
    }
}
