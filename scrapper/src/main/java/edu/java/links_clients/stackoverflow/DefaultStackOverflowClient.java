package edu.java.links_clients.stackoverflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.java.configuration.ApplicationConfig;
import edu.java.links_clients.dto.stckoverflow.AnswerInfo;
import edu.java.links_clients.dto.stckoverflow.AnswerItems;
import edu.java.links_clients.dto.stckoverflow.CommentInfo;
import edu.java.links_clients.dto.stckoverflow.CommentItems;
import edu.java.utility.EmptyJsonException;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import java.util.List;
import java.util.Objects;
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
public class DefaultStackOverflowClient implements StackOverflowClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultStackOverflowClient.class);
    private final WebClient webClient;

    @Autowired @Qualifier("stackOverflowRetry")
    Retry retry;

    @Autowired
    public DefaultStackOverflowClient(ApplicationConfig config) {
        String defaultUrl = config.listOfLinksSupported().stackoverflow();
        webClient = WebClient.builder()
            .baseUrl(defaultUrl)
            .defaultHeader("X-API-Access-Token", config.api().stackoverflowAccessToken())
            .defaultHeader("X-API-Key", config.api().stackoverflowKey())
            .build();
    }

    public DefaultStackOverflowClient(String baseUrl) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();

    }

    @Override
    public Optional<StackOverflowResponse> processQuestionUpdates(long questionId) {
        try {
            Mono<String> operation = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/questions/{id}/answers")
                    .queryParam("order", "desc")
                    .queryParam("sort", "activity")
                    .queryParam("site", "stackoverflow")
                    .build(questionId))
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
}
