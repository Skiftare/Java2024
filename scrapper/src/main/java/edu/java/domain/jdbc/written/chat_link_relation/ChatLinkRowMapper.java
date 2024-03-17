package edu.java.domain.jdbc.written.chat_link_relation;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ChatLinkRowMapper implements RowMapper<ChatLink> {
    @Override
    @SuppressWarnings("MagicNumber")
    public ChatLink mapRow(ResultSet rs, int rowNum) throws SQLException {
        ChatLink chatLink = new ChatLink();
        chatLink.setChatId(rs.getLong(1));
        chatLink.setLinkId(rs.getLong(2));
        return chatLink;
    }
}
