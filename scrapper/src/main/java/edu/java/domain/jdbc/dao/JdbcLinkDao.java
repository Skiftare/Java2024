package edu.java.domain.jdbc.dao;

import edu.java.domain.jdbc.written.link.Link;
import edu.java.domain.jdbc.written.link.LinkRowMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkDao {
    private final JdbcClient jdbcClient;
    private final LinkRowMapper linkRowMapper = new LinkRowMapper();

    public List<Link> getAll() {
        String sql = "SELECT * FROM link";
        return jdbcClient.sql(sql)
            .query(linkRowMapper).list();
    }

    public Optional<Link> findByUrl(String url) {
        String sql = "SELECT * FROM link WHERE url = ?";
        return jdbcClient.sql(sql)
            .param(url)
            .query(linkRowMapper).optional();
    }

    public Optional<Link> findById(long id) {
        String sql = "SELECT * FROM link WHERE id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(linkRowMapper).optional();
    }

    public List<Link> getByLastUpdate(OffsetDateTime dateTime) {
        String sql = "SELECT * FROM link WHERE last_update_at < ?";
        return jdbcClient.sql(sql)
            .param(dateTime)
            .query(linkRowMapper).list();
    }

    public int save(Link link) {

        String sql = "INSERT INTO link(url, created_at, last_update_at) VALUES (?, ?, ?)";
        return jdbcClient.sql(sql)
            .params(link.getUrl(), link.getCreatedAt(), link.getLastUpdateAt())
            .update();
    }

    public void updateLastUpdateAtById(long id, OffsetDateTime dateTime) {
        String sql = "UPDATE link SET last_update_at = ? WHERE id = ?";
        jdbcClient.sql(sql)
            .params(dateTime, id)
            .update();
    }

    public int deleteByLink(String url) {
        String sql = "DELETE FROM link WHERE url = ?";
        return jdbcClient.sql(sql)
            .param(url)
            .update();
    }

    public void deleteByDataLinkId(long id) {
        String sql = "DELETE FROM link WHERE id = ?";
        jdbcClient.sql(sql)
            .param(id)
            .update();
    }
}
