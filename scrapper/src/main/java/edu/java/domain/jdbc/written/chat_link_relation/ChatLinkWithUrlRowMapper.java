package edu.java.domain.jdbc.written.chat_link_relation;

import edu.java.domain.dto_chat_links.ChatLinkWithUrl;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ChatLinkWithUrlRowMapper implements RowMapper<ChatLinkWithUrl> {
    @Override
    @SuppressWarnings("MagicNumber")
    public ChatLinkWithUrl mapRow(ResultSet rs, int rowNum) throws SQLException {
        ChatLinkWithUrl chatLinkWithUrl = new ChatLinkWithUrl();
        chatLinkWithUrl.setChatId(rs.getLong(1));
        chatLinkWithUrl.setLinkId(rs.getLong(2));
        chatLinkWithUrl.setUrl(rs.getString(3));
        return chatLinkWithUrl;
    }
}
