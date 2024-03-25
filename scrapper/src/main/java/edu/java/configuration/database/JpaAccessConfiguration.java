package edu.java.configuration.database;

import edu.java.api.BotServiceForWebClient;
import edu.java.domain.jpa.services.JpaChatService;
import edu.java.domain.jpa.services.JpaLinkService;
import edu.java.domain.jpa.services.JpaLinkUpdaterScheduler;
import edu.java.domain.jpa.written.JpaChatRepository;
import edu.java.domain.jpa.written.JpaLinkRepository;
import edu.java.links_clients.LinkHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JpaAccessConfiguration {

    @Bean
    public JpaChatService jpaChatService(JpaChatRepository jpaChatRepository) {
        return new JpaChatService(jpaChatRepository);
    }

    @Bean
    public JpaLinkService jpaLinkService(
        JpaChatRepository jpaChatRepository,
        JpaLinkRepository jpaLinkRepository
    ) {
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
        return new JpaLinkUpdaterScheduler(
            jpaLinkRepository,
            botService,
            webResourceHandler
        );
    }
}
