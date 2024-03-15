package edu.java.database.dao;

import edu.java.database.dto.ChatDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public class ChatDao {
    private final JdbcTemplate jdbcTemplate;

    public ChatDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void add(ChatDto chat) {
        String sql = "INSERT INTO chat (chat_id, created_at) VALUES (?, ?)";
        jdbcTemplate.update(sql, chat.chatId(), chat.createdAt());

    }

    @Transactional
    public void remove(Long id) {
        String sql = "DELETE FROM chat WHERE chat_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<ChatDto> findAll() {
        String sql = "SELECT * FROM chat";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ChatDto(rs.getLong("chat_id"), rs.getTimestamp("created_at").toLocalDateTime()));
    }
}
