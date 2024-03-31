package edu.java.links_clients.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.java.backoff_policy.LinearBackOffPolicy;
import edu.java.configuration.ApplicationConfig;
import edu.java.links_clients.dto.github.GithubActions;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Service
public class DefaultGitHubClient implements GitHubClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGitHubClient.class);
    private final WebClient webClient;
    private final ApplicationConfig.ServiceProperties serviceProperties;
    private final HashMap<Integer, RetryTemplate> retryStrategies = new HashMap<>();
    private static final long MAX_INTERVAL = 5000L;
    private static final long DEFAULT_INCREMENT = 100L;

    @Autowired
    public DefaultGitHubClient(ApplicationConfig config) {
        String defaultUrl = "https://api." + config.listOfLinksSupported().github();
        webClient = WebClient.builder()
            .baseUrl(defaultUrl)
            .build();
        this.serviceProperties = config.github();
        initRetryStrategies();
    }

    public DefaultGitHubClient(String baseUrl, ApplicationConfig.ServiceProperties serviceProperties) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();

        this.serviceProperties = serviceProperties;
        initRetryStrategies();
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

    private void initRetryStrategies() {
        serviceProperties.templates().forEach((code, template) -> {
            RetryTemplate retryTemplate = new RetryTemplate();
            switch (template.type()) {
                case "exponential":
                    ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
                    exponentialBackOffPolicy.setInitialInterval(template.delay().toMillis());
                    exponentialBackOffPolicy.setMultiplier(2.0);
                    exponentialBackOffPolicy.setMaxInterval(MAX_INTERVAL);
                    retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);
                    break;
                case "linear":
                    LinearBackOffPolicy linearBackOffPolicy = new LinearBackOffPolicy(
                        template.delay().toMillis(),
                        template.maxAttempts(),
                        DEFAULT_INCREMENT
                    );
                    retryTemplate.setBackOffPolicy(linearBackOffPolicy);
                    break;
                case "constant":
                default:
                    FixedBackOffPolicy constantBackOffPolicy = new FixedBackOffPolicy();
                    constantBackOffPolicy.setBackOffPeriod(template.delay().toMillis());
                    retryTemplate.setBackOffPolicy(constantBackOffPolicy);
                    break;
            }

            SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
            retryPolicy.setMaxAttempts(template.maxAttempts());

            retryTemplate.setRetryPolicy(retryPolicy);

            retryStrategies.put(code, retryTemplate);
        });
    }
}
