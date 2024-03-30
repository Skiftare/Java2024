package edu.java.domain.jdbc.dao;

import edu.java.domain.jdbc.written.chat.Chat;
import edu.java.domain.jdbc.written.chat.ChatRowMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatDao {
    private final JdbcClient jdbcClient;
    private final Logger logger = LoggerFactory.getLogger(JdbcChatDao.class);
    private final ChatRowMapper chatRowMapper;

    public List<Chat> getAll() {
        String sql = "SELECT * FROM chat";
        return jdbcClient.sql(sql)
            .query(chatRowMapper).list();
    }

    public Optional<Chat> getByTgChatId(long tgChatId) {
        logger.info("Getting chat by chat_id: " + tgChatId + " in the ChatDaoJdbc");
        String sql = "SELECT * FROM chat WHERE chat_id = ?";
        return jdbcClient.sql(sql)
            .param(tgChatId)
            .query(chatRowMapper)
            .optional();
    }
    public Optional<Chat> getByDataId(long tgChatId) {
        logger.info("Getting chat by id: " + tgChatId + " in the ChatDaoJdbc");
        String sql = "SELECT * FROM chat WHERE id = ?";
        return jdbcClient.sql(sql)
            .param(tgChatId)
            .query(chatRowMapper)
            .optional();
    }

    public int save(Chat chat) {
        String sql = "INSERT INTO chat(chat_id, created_at) VALUES (?, ?)";
        return jdbcClient.sql(sql)
            .params(chat.getTgChatId(), chat.getCreatedAt())
            .update();
    }

    public int deleteByTgChatId(long tgChatId) {
        String sql = "DELETE FROM chat WHERE chat_id = ?";
        return jdbcClient.sql(sql)
            .param(tgChatId)
            .update();
    }
}
