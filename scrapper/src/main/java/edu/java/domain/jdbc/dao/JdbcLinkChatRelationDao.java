package edu.java.domain.jdbc.dao;

import edu.java.domain.jdbc.written.chat_link_relation.ChatLinkRelation;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLinkRelationRowMapper;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLinkWithUrl;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLinkWithUrlRowMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")

public class JdbcLinkChatRelationDao {

    private final JdbcClient jdbcClient;
    private final ChatLinkRelationRowMapper chatLinkRowMapper;
    private final ChatLinkWithUrlRowMapper chatLinkWithUrlRowMapper;

    public List<ChatLinkRelation> getByLinkId(long id) {
        String sql = "SELECT * FROM link_chat_relation WHERE id_of_link = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkRowMapper).list();
    }

    public List<ChatLinkRelation> getAll() {
        String sql = "SELECT * FROM link_chat_relation";
        return jdbcClient.sql(sql)
            .query(chatLinkRowMapper).list();
    }

    public List<ChatLinkRelation> getByChatId(long id) {
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

    public int save(ChatLinkRelation chatLink) {
        String sql = "INSERT INTO link_chat_relation(id_of_chat, id_of_link) VALUES (?, ?)";
        return jdbcClient.sql(sql)
            .params(chatLink.getDataChatId(), chatLink.getDataLinkId())
            .update();
    }

    public int delete(long chatId, long linkId) {
        String sql = "DELETE FROM link_chat_relation WHERE id_of_chat = ? AND id_of_link = ?";
        return jdbcClient.sql(sql)
            .params(chatId, linkId)
            .update();
    }
}
