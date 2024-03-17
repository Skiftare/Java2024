package edu.java.configuration;

import edu.java.database.services.interfaces.LinkService;
import edu.java.domain.jdbc.dao.JdbcChatDao;
import edu.java.domain.jdbc.dao.JdbcLinkChatRelationDao;
import edu.java.domain.jdbc.dao.JdbcLinkDao;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*
@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    @Bean
    public LinkService linkService(
            JdbcLinkDao linkDao,
            JdbcChatDao chatDao,
            JdbcLinkChatRelationDao linkChatRelationDao
    ) {
        return new LinkService(linkDao, chatDao, linkChatRelationDao);
    }

    //
}
*/
