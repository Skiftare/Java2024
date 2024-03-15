package edu.java.database.dao;

import edu.java.database.dto.LinkChatRelationDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public class LinkChatRelationDao {
    private final JdbcTemplate jdbcTemplate;

    public LinkChatRelationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void add(LinkChatRelationDto relation) {
        String sql = "INSERT INTO link_chat_relation (id, id_of_chat, id_of_link) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, relation.id(), relation.chatId(), relation.linkId());
    }

    @Transactional
    public void remove(LinkChatRelationDto relation) {
        String sql = "DELETE FROM link_chat_relation WHERE id = ?";
        jdbcTemplate.update(sql, relation.id());
    }

    public List<LinkChatRelationDto> findAll() {
        String sql = "SELECT * FROM link_chat_relation";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new LinkChatRelationDto(rs.getLong("id"), rs.getLong("id_of_chat"), rs.getLong("id_of_link")));
    }
}
