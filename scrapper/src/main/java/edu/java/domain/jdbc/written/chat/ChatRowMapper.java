package edu.java.domain.jdbc.written.chat;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

@Component
public class ChatRowMapper implements RowMapper<Chat> {
    @Override
    @SuppressWarnings("MagicNumber")
    public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
        Chat chat = new Chat();
        chat.setId(rs.getLong(1));
        chat.setTgChatId(rs.getLong(2));
        chat.setCreatedAt(rs.getObject(3, OffsetDateTime.class));
        return chat;
    }
}
