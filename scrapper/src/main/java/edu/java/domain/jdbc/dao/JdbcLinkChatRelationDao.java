package edu.java.domain.jdbc.dao;

import edu.java.database.dto.LinkChatRelationDto;
import edu.java.domain.dto_chat_links.ChatLinkWithUrl;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLink;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLinkRowMapper;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLinkWithTgChat;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLinkWithTgChatRowMapper;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLinkWithUrlRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcLinkChatRelationDao {
    private final JdbcClient jdbcClient;
    private final ChatLinkRowMapper chatLinkRowMapper;
    private final ChatLinkWithUrlRowMapper chatLinkWithUrlRowMapper;
    private final ChatLinkWithTgChatRowMapper chatLinkWithTgChatRowMapper;

    public List<ChatLink> getAll() {
        String sql = "SELECT * FROM chat_link";
        return jdbcClient.sql(sql)
            .query(chatLinkRowMapper).list();
    }

    public List<ChatLink> getByChatId(long id) {
        String sql = "SELECT * FROM chat_link WHERE chat_id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkRowMapper).list();
    }

    public List<ChatLinkWithUrl> getByChatIdJoinLink(long id) {
        String sql = """
            SELECT cl.chat_id, cl.link_id, l.url
            FROM chat_link cl
            JOIN link l ON l.id = cl.link_id
            WHERE chat_id = ?""";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkWithUrlRowMapper).list();
    }

    public List<ChatLink> getByLinkId(long id) {
        String sql = "SELECT * FROM chat_link WHERE link_id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkRowMapper).list();
    }

    public List<ChatLinkWithTgChat> getByLinkIdIdJoinChat(long id) {
        String sql = """
            SELECT cl.chat_id, cl.link_id, c.tg_chat_id
            FROM chat_link cl
            JOIN chat c ON c.id = cl.chat_id
            WHERE link_id = ?""";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkWithTgChatRowMapper).list();
    }

    public int save(ChatLink chatLink) {
        String sql = "INSERT INTO chat_link(chat_id, link_id) VALUES (?, ?)";
        return jdbcClient.sql(sql)
            .params(chatLink.getChatId(), chatLink.getLinkId())
            .update();
    }

    public int delete(long chatId, long linkId) {
        String sql = "DELETE FROM chat_link WHERE chat_id = ? AND link_id = ?";
        return jdbcClient.sql(sql)
            .params(chatId, linkId)
            .update();
    }
}
