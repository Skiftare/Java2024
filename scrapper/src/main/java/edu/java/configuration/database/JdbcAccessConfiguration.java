package edu.java.configuration.database;

import edu.java.api.BotServiceForWebClient;
import edu.java.database.services.interfaces.LinkService;
import edu.java.database.services.interfaces.LinkUpdater;
import edu.java.database.services.interfaces.TgChatService;
import edu.java.domain.jdbc.dao.JdbcChatDao;
import edu.java.domain.jdbc.dao.JdbcLinkChatRelationDao;
import edu.java.domain.jdbc.dao.JdbcLinkDao;
import edu.java.domain.jdbc.services.JdbcChatService;
import edu.java.domain.jdbc.services.JdbcLinkService;
import edu.java.domain.jdbc.services.JdbcUpdateScheduler;
import edu.java.links_clients.LinkHandler;
import java.util.logging.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    private final Logger logger = Logger.getLogger(JdbcAccessConfiguration.class.getName());
    @Bean
    public LinkService linkService(JdbcLinkDao linkDao, JdbcChatDao chatDao, JdbcLinkChatRelationDao chatLinkDao) {
        logger.info("Creating JdbcLinkService bean");
        return new JdbcLinkService(chatDao, linkDao, chatLinkDao);
    }

    @Bean
    public TgChatService tgChatService(JdbcChatDao chatDao, JdbcLinkDao linkDao, JdbcLinkChatRelationDao chatLinkDao) {
        logger.info("Creating JdbcChatService bean");
        return new JdbcChatService(chatDao, linkDao, chatLinkDao);
    }

    @Bean
    public LinkUpdater linkUpdater(
        BotServiceForWebClient botServiceForWebClient, JdbcLinkDao linkDao, JdbcLinkChatRelationDao chatLinkDao,
        LinkHandler webResourceHandler, JdbcChatDao chatDao
    ) {
        logger.info("Creating JdbcUpdateScheduler bean");
        return new JdbcUpdateScheduler(
            linkDao,
            chatLinkDao,
            chatDao,
            botServiceForWebClient,
            webResourceHandler
        );
    }
}

