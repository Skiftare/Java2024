package edu.java.links_clients.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.java.configuration.ApplicationConfig;
import edu.java.links_clients.dto.github.GithubActions;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

@Service
public class DefaultGitHubClient implements GitHubClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGitHubClient.class);
    private final WebClient webClient;

    @Autowired @Qualifier("gitHubRetry")
    Retry retry;

    @Autowired
    public DefaultGitHubClient(ApplicationConfig config) {
        String defaultUrl = "https://api." + config.listOfLinksSupported().github();
        webClient = WebClient.builder()
            .baseUrl(defaultUrl)
            .build();
    }

    public DefaultGitHubClient(String baseUrl) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();

    }

    @Override
    public Optional<GitHubResponse> processRepositoryUpdates(String owner, String repo) {
        try {
            Mono<String> operation = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/repos/{owner}/{repo}/events")
                    .queryParam("per_page", 1)
                    .build(owner, repo))
                .retrieve()
                .bodyToMono(String.class);
            if (retry != null) {
                operation.transformDeferred(RetryOperator.of(retry));
            }
            return operation
                .mapNotNull(this::parseJson)
                .block();
        } catch (WebClientException | NullPointerException e) {
            LOGGER.error(e.getMessage());
            return Optional.empty();
        }
    }

    public List<GithubActions> getActionsInfo(String owner, String repo) {
        return webClient.get()
            .uri("/repos/{owner}/{repo}/activity", owner, repo)
            .retrieve()
            .bodyToFlux(GithubActions.class)
            .collectList()
            .block();
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
