package edu.java.domain.jdbc.written.chat_link_relation;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatLinkWithTgChatRowMapper implements RowMapper<ChatLinkWithTgChat> {
    @Override
    @SuppressWarnings("MagicNumber")
    public ChatLinkWithTgChat mapRow(ResultSet rs, int rowNum) throws SQLException {
        ChatLinkWithTgChat chatLinkWithTgChat = new ChatLinkWithTgChat();
        chatLinkWithTgChat.setDataChatId(rs.getLong(1));
        chatLinkWithTgChat.setDataLinkId(rs.getLong(2));
        chatLinkWithTgChat.setTgChatId(rs.getLong(3));
        return chatLinkWithTgChat;
    }
}
