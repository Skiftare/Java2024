package edu.java.stackoverflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.java.configuration.ApplicationConfig;
import edu.java.utility.EmptyJsonException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Service
public class DefaultStackOverflowClient implements StackOverflowClient {
    private final WebClient webClient;
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultStackOverflowClient.class);

    @Autowired
    public DefaultStackOverflowClient(ApplicationConfig config) {
        String defaultUrl = config.stackOverflow().defaultUrl();
        webClient = WebClient.builder()
            .baseUrl(defaultUrl)
            .build();

    }

    public DefaultStackOverflowClient(String baseUrl) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
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
}
