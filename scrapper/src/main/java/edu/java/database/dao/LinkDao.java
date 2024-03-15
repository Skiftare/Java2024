package edu.java.database.dao;

import edu.java.database.dto.LinkDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public class LinkDao {
    private final JdbcTemplate jdbcTemplate;

    public LinkDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void add(LinkDto link) {
        String sql = "INSERT INTO link (link_id, url, created_at) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, link.linkId(), link.url(), link.createdAt());
    }

    @Transactional
    public void remove(LinkDto link) {
        String sql = "DELETE FROM link WHERE link_id = ?";
        jdbcTemplate.update(sql, link.linkId());
    }

    public List<LinkDto> findAll() {
        String sql = "SELECT * FROM link";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new LinkDto(rs.getLong("link_id"), rs.getString("url"), rs.getTimestamp("created_at").toLocalDateTime()));
    }

    public List<LinkDto> listAllSortedByUpdateDate() {
        String sql = "SELECT * FROM link ORDER BY updated_at DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new LinkDto(rs.getLong("link_id"), rs.getString("url"), rs.getTimestamp("created_at").toLocalDateTime()));
    }
}
