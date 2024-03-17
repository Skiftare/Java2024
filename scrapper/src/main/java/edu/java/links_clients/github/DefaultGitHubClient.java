package edu.java.links_clients.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.java.configuration.ApplicationConfig;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
@Service
public class DefaultGitHubClient implements GitHubClient {
    private final WebClient webClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGitHubClient.class);

   @Autowired
    public DefaultGitHubClient(ApplicationConfig config) {
        String defaultUrl = config.gitHub().defaultUrl();
        webClient = WebClient.builder()
            .baseUrl(defaultUrl)
            .build();
    }

    public DefaultGitHubClient(String baseUrl) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public Optional<GitHubResponse> processUpdates(String link) {
       try{
           URI linkUri = URI.create(link);
           String path = linkUri.getPath();
           String[] parts = path.split("/");

           if (parts.length < 3) {
               return Optional.empty();
           }

           String owner = parts[1];
           String repo = parts[2];

           return processRepositoryUpdates(owner, repo);
       }
       catch (IllegalArgumentException e) {
           LOGGER.error(e.getMessage());
           return Optional.empty();
       }

    }


    @Override
    public Optional<GitHubResponse> processRepositoryUpdates(String owner, String repo) {
        try {
            return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/repos/{owner}/{repo}/events")
                    .queryParam("per_page", 1)
                    .build(owner, repo))
                .retrieve()
                .bodyToMono(String.class)
                .mapNotNull(this::parseJson)
                .block();
        } catch (WebClientException | NullPointerException e) {
            LOGGER.error(e.getMessage());
            return Optional.empty();
        }
    }
    private Optional<GitHubResponse> processPullRequestUpdates(String owner, String repo, String pullRequestId) {
        try {
            return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/repos/{owner}/{repo}/pulls/{pullRequestId}/events")
                    .queryParam("per_page", 1)
                    .build(owner, repo, pullRequestId))
                .retrieve()
                .bodyToMono(String.class)
                .mapNotNull(this::parseJson)
                .block();
        } catch (WebClientException | NullPointerException e) {
            LOGGER.error(e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<GitHubResponse> processIssueUpdates(String owner, String repo, String issueId) {
        try {
            return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/repos/{owner}/{repo}/issues/{issueId}/events")
                    .queryParam("per_page", 1)
                    .build(owner, repo, issueId))
                .retrieve()
                .bodyToMono(String.class)
                .mapNotNull(this::parseJson)
                .block();
        } catch (WebClientException | NullPointerException e) {
            LOGGER.error(e.getMessage());
            return Optional.empty();
        }
    }


    private Optional<GitHubResponse> parseJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            List<GitHubResponse> responses = objectMapper.readValue(json, new TypeReference<>() {
            });
            return Optional.ofNullable(responses)
                .filter(list -> !list.isEmpty())
                .map(List::getFirst);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            return Optional.empty();
        }
    }
}
