package edu.java.domain.jdbc.written.chat_link_relation;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatLinkRelationRowMapper implements RowMapper<ChatLinkRelation> {
    @Override
    @SuppressWarnings("MagicNumber")
    public ChatLinkRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
        ChatLinkRelation chatLink = new ChatLinkRelation();
        chatLink.setDataChatId(rs.getLong(1));
        chatLink.setDataLinkId(rs.getLong(2));
        return chatLink;
    }
}
