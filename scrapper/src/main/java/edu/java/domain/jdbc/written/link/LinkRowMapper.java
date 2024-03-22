package edu.java.domain.jdbc.written.link;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LinkRowMapper implements RowMapper<Link> {
    @Override
    @SuppressWarnings("MagicNumber")
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        Link link = new Link();
        link.setDataLinkId(rs.getLong(1));
        link.setUrl(rs.getString(2));
        link.setCreatedAt(rs.getObject(3, OffsetDateTime.class));
        link.setLastUpdateAt(rs.getObject(4, OffsetDateTime.class));
        return link;
    }
}
