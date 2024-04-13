package edu.java.configuration.database;

import edu.java.api.BotServiceForWebClient;
import edu.java.domain.jpa.services.JpaChatService;
import edu.java.domain.jpa.services.JpaLinkService;
import edu.java.domain.jpa.services.JpaLinkUpdaterScheduler;
import edu.java.domain.jpa.written.JpaChatRepository;
import edu.java.domain.jpa.written.JpaLinkRepository;
import edu.java.links_clients.LinkHandler;
import java.util.logging.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    private final Logger logger = Logger.getLogger(JpaAccessConfiguration.class.getName());

    @Bean
    public JpaChatService jpaChatService(JpaChatRepository jpaChatRepository) {
        logger.info("Creating JpaChatService bean");
        return new JpaChatService(jpaChatRepository);
    }

    @Bean
    public JpaLinkService jpaLinkService(
        JpaChatRepository jpaChatRepository,
        JpaLinkRepository jpaLinkRepository
    ) {
        logger.info("Creating JpaLinkService bean");
        return new JpaLinkService(
            jpaChatRepository,
            jpaLinkRepository
        );
    }

    @Bean
    public JpaLinkUpdaterScheduler jpaLinkUpdaterScheduler(
        JpaLinkRepository jpaLinkRepository,
        BotServiceForWebClient botService,
        LinkHandler webResourceHandler
    ) {
        logger.info("Creating JpaLinkUpdaterScheduler bean");
        return new JpaLinkUpdaterScheduler(
            jpaLinkRepository,
            botService,
            webResourceHandler
        );
    }
}
