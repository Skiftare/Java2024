package edu.java.database.services.entities;


import edu.java.database.dao.ChatDao;
import edu.java.database.dto.ChatDto;
import edu.java.database.services.interfaces.TgChatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class JdbcTgChatService implements TgChatService {
    private final ChatDao chatDao;

    public JdbcTgChatService(ChatDao chatDao) {
        this.chatDao = chatDao;
    }

    @Transactional
    @Override
    public void register(long tgChatId) {
        ChatDto chat = new ChatDto(tgChatId, LocalDateTime.now());
        chatDao.add(chat);
    }

    @Transactional
    @Override
    public void unregister(long tgChatId) {
        chatDao.remove(tgChatId);
    }
}
