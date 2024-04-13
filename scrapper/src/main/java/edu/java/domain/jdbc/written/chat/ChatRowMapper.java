package edu.java.domain.jdbc.written.chat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.logging.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatRowMapper implements RowMapper<Chat> {
    private final Logger logger = Logger.getLogger(ChatRowMapper.class.getName());

    @Override
    @SuppressWarnings("MagicNumber")
    public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
        Chat chat = new Chat();
        logger.info("Mapping chat with data_id: " + rs.getLong(1));
        chat.setTgChatId(rs.getLong(2));
        chat.setCreatedAt(rs.getObject(3, OffsetDateTime.class));
        return chat;
    }
}
