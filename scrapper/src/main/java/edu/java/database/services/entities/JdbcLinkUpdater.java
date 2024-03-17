package edu.java.database.services.entities;

import edu.java.api.entities.requests.LinkUpdateRequest;
import edu.java.api.web.WebClientForBotCommunication;
import edu.java.configuration.ApplicationConfig;
import edu.java.database.dto.LinkDto;
import edu.java.database.services.interfaces.LinkService;
import edu.java.database.services.interfaces.LinkUpdater;
import edu.java.links_clients.github.DefaultGitHubClient;
import edu.java.links_clients.github.GitHubResponse;
import edu.java.links_clients.stackoverflow.DefaultStackOverflowClient;
import edu.java.links_clients.stackoverflow.StackOverflowResponse;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkUpdater implements LinkUpdater {
    private final LinkService linkService;
    private final DefaultGitHubClient githubClient;
    private final DefaultStackOverflowClient stackOverFlowClient;
    private final WebClientForBotCommunication botClient;
    private ApplicationConfig.Scheduler scheduler;


    public JdbcLinkUpdater(
        LinkService linkService,
        DefaultGitHubClient githubClient,
        DefaultStackOverflowClient stackOverFlowClient,
        WebClientForBotCommunication botClient,
        ApplicationConfig.Scheduler scheduler
    ) {
        this.linkService = linkService;
        this.githubClient = githubClient;
        this.stackOverFlowClient = stackOverFlowClient;
        this.botClient = botClient;
        this.scheduler = scheduler;
    }

    @Override
    public int update() {
        // TODO: Implement this method based on your business logic
        return 0;
    }

    @Override
    public void checkForUpdates() {
        linkService.listAll().stream()
            .filter(link -> Duration.between(link.createdAt(), LocalDateTime.now()).toMillis() >
                scheduler.parserDelay().toMillis());
            //.forEach(this::update);
    }

    /*private void update(LinkDto linkDto) {
        URI link = URI.create(linkDto.url());
        Optional<GitHubResponse> gitHubResponse = githubClient.processUpdates(link.toString());
        Optional<StackOverflowResponse> stackOverflowResponse = stackOverFlowClient.processUpdates(link.toString());
        List<String> messages = Stream.of(gitHubResponse, stackOverflowResponse)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(response -> response.toMessage(link))
            .collect(Collectors.toList());
        messages.forEach(message -> botClient.sendUpdate(new LinkUpdateRequest(
            JdbcLinkService., link.toString(), message)));

    }*/

}
