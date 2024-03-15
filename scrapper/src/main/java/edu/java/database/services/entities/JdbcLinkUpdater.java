package edu.java.database.services.entities;


import edu.java.api.web.WebClientForBotCommunication;
import edu.java.database.services.interfaces.LinkUpdater;
import edu.java.github.DefaultGitHubClient;
import edu.java.stackoverflow.DefaultStackOverflowClient;
import org.springframework.stereotype.Service;


import edu.java.database.services.interfaces.LinkService;
import edu.java.database.services.interfaces.LinkUpdater;
import edu.java.database.dto.LinkDto;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collection;

@Service
public class JdbcLinkUpdater implements LinkUpdater {
    private final LinkService linkService;
    private final DefaultGitHubClient githubClient;
    private final DefaultStackOverflowClient stackOverFlowClient;
    private final WebClientForBotCommunication botClient;

    public JdbcLinkUpdater(LinkService linkService, DefaultGitHubClient githubClient, DefaultStackOverflowClient stackOverFlowClient, WebClientForBotCommunication botClient) {
        this.linkService = linkService;
        this.githubClient = githubClient;
        this.stackOverFlowClient = stackOverFlowClient;
        this.botClient = botClient;
    }

    @Override
    public int update() {
        // TODO: Implement this method based on your business logic
        return 0;
    }

    @Override
    public void checkForUpdates() {
        linkService.listAll().stream()
            .map(LinkDto::url)
            .map(URI::create)
            .filter(url -> githubClient.checkForUpdates(url) || stackOverFlowClient.checkForUpdates(url))
            .forEach(botClient::sendUpdateNotification);
        }
    }
}
