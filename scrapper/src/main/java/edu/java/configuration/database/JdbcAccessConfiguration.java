package edu.java.configuration.database;

import edu.java.database.services.interfaces.LinkService;
import edu.java.database.services.interfaces.TgChatService;
import edu.java.domain.jdbc.dao.JdbcChatDao;
import edu.java.domain.jdbc.dao.JdbcLinkChatRelationDao;
import edu.java.domain.jdbc.dao.JdbcLinkDao;
import edu.java.domain.jdbc.services.JdbcChatService;
import edu.java.domain.jdbc.services.JdbcLinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    @Bean
    public LinkService linkService(JdbcLinkDao linkDao, JdbcChatDao chatDao, JdbcLinkChatRelationDao chatLinkDao) {
        return new JdbcLinkService(chatDao, linkDao, chatLinkDao);
    }

    @Bean
    public TgChatService tgChatService(JdbcChatDao chatDao, JdbcLinkDao linkDao, JdbcLinkChatRelationDao chatLinkDao) {
        return new JdbcChatService(chatDao, linkDao, chatLinkDao);
    }
}
