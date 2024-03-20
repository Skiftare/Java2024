package edu.java.domain.jdbc.written.chat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatRowMapper implements RowMapper<Chat> {
    @Override
    @SuppressWarnings("MagicNumber")
    public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
        Chat chat = new Chat();
        chat.setTgChatId(rs.getLong(1));
        chat.setCreatedAt(rs.getObject(2, OffsetDateTime.class));
        return chat;
    }
}
