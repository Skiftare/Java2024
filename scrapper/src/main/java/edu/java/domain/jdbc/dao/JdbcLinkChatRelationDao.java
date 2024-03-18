package edu.java.domain.jdbc.dao;

import edu.java.domain.dto_chat_links.ChatLinkWithUrl;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLink;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLinkRowMapper;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLinkWithTgChat;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLinkWithTgChatRowMapper;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLinkWithUrlRowMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkChatRelationDao {

    private static JdbcClient jdbcClient;
    private static ChatLinkRowMapper chatLinkRowMapper;
    private final ChatLinkWithUrlRowMapper chatLinkWithUrlRowMapper;
    private final ChatLinkWithTgChatRowMapper chatLinkWithTgChatRowMapper;

    public static List<ChatLink> getByLinkId(long id) {
        String sql = "SELECT * FROM link_chat_relation WHERE id_of_link = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkRowMapper).list();
    }

    public List<ChatLink> getAll() {
        String sql = "SELECT * FROM link_chat_relation";
        return jdbcClient.sql(sql)
            .query(chatLinkRowMapper).list();
    }

    public List<ChatLink> getByChatId(long id) {
        String sql = "SELECT * FROM link_chat_relation WHERE id_of_chat = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkRowMapper).list();
    }

    public List<ChatLinkWithUrl> getByChatIdJoinLink(long id) {
        String sql = """
            SELECT lcr.id_of_chat, lcr.id_of_link, l.url
            FROM link_chat_relation lcr
            JOIN link l ON l.id = lcr.id_of_link
            WHERE id_of_chat = ?""";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkWithUrlRowMapper).list();
    }

    public List<ChatLinkWithTgChat> getByLinkIdIdJoinChat(long id) {
        String sql = """
            SELECT lcr.id_of_chat, lcr.id_of_link, c.tg_chat_id
            FROM link_chat_relation lcr
            JOIN chat c ON c.chat_id = lcr.id_of_chat
            WHERE id_of_link = ?""";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkWithTgChatRowMapper).list();
    }

    public int save(ChatLink chatLink) {
        String sql = "INSERT INTO link_chat_relation(id_of_chat, id_of_link) VALUES (?, ?)";
        return jdbcClient.sql(sql)
            .params(chatLink.getChatId(), chatLink.getLinkId())
            .update();
    }

    public int delete(long chatId, long linkId) {
        String sql = "DELETE FROM link_chat_relation WHERE id_of_chat = ? AND id_of_link = ?";
        return jdbcClient.sql(sql)
            .params(chatId, linkId)
            .update();
    }
}
