package edu.java.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.java.configuration.ApplicationConfig;
import edu.java.utility.GlobalExceptionHandler;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class DefaultGitHubClient implements GitHubClient {
    private final WebClient webClient;

    @Autowired
    public DefaultGitHubClient(ApplicationConfig config) {
        String defaultUrl = config.gitHubUrl().defaultUrl();
        webClient = WebClient.builder().baseUrl(defaultUrl).build();
    }

    public DefaultGitHubClient(String baseUrl) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public Optional<GitHubResponse> processRepositoryUpdates(String owner, String repo) {
        try {
            return Optional.ofNullable(webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/repos/{owner}/{repo}/events")
                    .queryParam("per_page", 1)
                    .build(owner, repo))
                .retrieve()
                .bodyToMono(String.class)
                .mapNotNull(this::parseJson)
                .block());
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    private GitHubResponse parseJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            List<GitHubResponse> responses = objectMapper.readValue(json, new TypeReference<>() {
            });
            return responses.getFirst();
        } catch (JsonProcessingException e) {
            Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
            logger.error(e.getMessage());
            return null;
        }
    }
}
