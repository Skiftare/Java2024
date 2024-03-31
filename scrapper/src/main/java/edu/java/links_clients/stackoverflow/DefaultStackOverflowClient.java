package edu.java.links_clients.stackoverflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.java.backoff_policy.LinearBackOffPolicy;
import edu.java.configuration.ApplicationConfig;
import edu.java.links_clients.dto.stckoverflow.AnswerInfo;
import edu.java.links_clients.dto.stckoverflow.AnswerItems;
import edu.java.links_clients.dto.stckoverflow.CommentInfo;
import edu.java.links_clients.dto.stckoverflow.CommentItems;
import edu.java.utility.EmptyJsonException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
public class DefaultStackOverflowClient implements StackOverflowClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultStackOverflowClient.class);
    private final WebClient webClient;
    private HashMap<Integer, RetryTemplate> retryStrategies = new HashMap<>();
    private final ApplicationConfig.ServiceProperties serviceProperties;

    @Autowired
    public DefaultStackOverflowClient(ApplicationConfig config) {
        String defaultUrl = config.listOfLinksSupported().stackoverflow();
        webClient = WebClient.builder()
            .baseUrl(defaultUrl)
            .build();
        serviceProperties = config.stackoverflow();
        initRetryStrategies();
    }

    public DefaultStackOverflowClient(String baseUrl, ApplicationConfig.ServiceProperties serviceProperties) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.serviceProperties = serviceProperties;
        initRetryStrategies();
    }

    @Override
    public Optional<StackOverflowResponse> processQuestionUpdates(long questionId) {
        try {
            return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/questions/{id}/answers")
                    .queryParam("order", "desc")
                    .queryParam("sort", "activity")
                    .queryParam("site", "stackoverflow")
                    .build(questionId))
                .retrieve()
                .bodyToMono(String.class)
                .mapNotNull(this::parseJson)
                .block();
        } catch (WebClientException | NullPointerException e) {
            LOGGER.error(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<StackOverflowResponse> parseJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode lastJsonAnswer = Optional.ofNullable(root.get("items"))
                .filter(items -> items.isArray() && !items.isEmpty())
                .map(items -> items.get(0))
                .orElseThrow(() -> new EmptyJsonException("No items in JSON"));
            return Optional.ofNullable(objectMapper.treeToValue(lastJsonAnswer, StackOverflowResponse.class));
        } catch (JsonProcessingException | EmptyJsonException e) {
            LOGGER.error(e.getMessage());
            return Optional.empty();
        }
    }

    public List<AnswerInfo> getAnswerInfoByQuestion(Long question) {
        return Objects.requireNonNull(webClient.get()
                .uri("/questions/{question}/answers?order=desc&site=stackoverflow", question)
                .retrieve()
                .bodyToMono(AnswerItems.class)
                .block())
            .getAnswerInfo();
    }

    public List<CommentInfo> getCommentInfoByQuestion(Long question) {
        return Objects.requireNonNull(webClient.get()
                .uri("/questions/{question}/comments?order=desc&sort=creation&site=stackoverflow", question)
                .retrieve()
                .bodyToMono(CommentItems.class)
                .block())
            .getCommentInfo();
    }

    private void initRetryStrategies() {
        serviceProperties.templates().forEach((code, template) -> {
            RetryTemplate retryTemplate = new RetryTemplate();
            switch (template.type()) {
                case "exponential":
                    ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
                    exponentialBackOffPolicy.setInitialInterval(template.delay().toMillis());
                    exponentialBackOffPolicy.setMultiplier(2.0);
                    exponentialBackOffPolicy.setMaxInterval(5000L);
                    retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);
                    break;
                case "linear":
                    LinearBackOffPolicy linearBackOffPolicy = new LinearBackOffPolicy(
                        template.delay().toMillis(),
                        template.maxAttempts(),
                        100L
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
