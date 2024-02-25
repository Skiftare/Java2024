package edu.java.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.java.utility.GlobalExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

public class DefaultGitHubClient implements GitHubClient {
    /*
    https://docs.github.com/en/rest/activity/events?apiVersion=2022-11-28#list-repository-events
     */
    @Value(value = "${api.github.defaultUrl}")
    private String defaultUrl;
    private final WebClient webClient;

    public DefaultGitHubClient() {
        webClient = WebClient.builder().baseUrl(defaultUrl).build();
    }

    public DefaultGitHubClient(String baseUrl) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public Optional<GitHubResponse> fetchRepositoryEvents(String owner, String repo) {
        return Optional.ofNullable(webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/repos/{owner}/{repo}/events")
                .queryParam("per_page", 1)
                .build(owner, repo))
            .retrieve()
            .bodyToMono(String.class)
            .mapNotNull(this::parseJson)
            .block());
    }

    public GitHubResponse parseJson(String json){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            List<GitHubResponse> responses = objectMapper.readValue(json, new TypeReference<>() {
            });
            return responses.getFirst();
        } catch (JsonProcessingException e){
            Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
            logger.error(e.getMessage());
            return null;
        }
    }
}
