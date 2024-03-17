package edu.java.domain.jdbc.written.chat_link_relation;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ChatLinkWithTgChatRowMapper implements RowMapper<ChatLinkWithTgChat> {
    @Override
    @SuppressWarnings("MagicNumber")
    public ChatLinkWithTgChat mapRow(ResultSet rs, int rowNum) throws SQLException {
        ChatLinkWithTgChat chatLinkWithTgChat = new ChatLinkWithTgChat();
        chatLinkWithTgChat.setChatId(rs.getLong(1));
        chatLinkWithTgChat.setLinkId(rs.getLong(2));
        chatLinkWithTgChat.setTgChatId(rs.getLong(3));
        return chatLinkWithTgChat;
    }
}
