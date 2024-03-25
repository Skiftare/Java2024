package edu.java.domain.jdbc.written.link;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Link {
    private long dataLinkId;
    private String url;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastUpdateAt;

    public static Link makeLink(String url, OffsetDateTime createdAt, OffsetDateTime lastUpdateAt) {
        Link link = new Link();
        link.setUrl(url);
        link.setCreatedAt(createdAt);
        link.setLastUpdateAt(lastUpdateAt);
        return link;
    }

}
