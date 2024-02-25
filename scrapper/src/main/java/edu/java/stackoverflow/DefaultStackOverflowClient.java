package edu.java.stackoverflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.java.configuration.ApplicationConfig;
import edu.java.utility.GlobalExceptionHandler;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class DefaultStackOverflowClient implements StackOverflowClient {
    private final WebClient webClient;

    @Autowired
    public DefaultStackOverflowClient(ApplicationConfig config) {
        String defaultUrl = config.stackOverflowUrl().defaultUrl();
        webClient = WebClient.builder().baseUrl(defaultUrl).build();
    }

    public DefaultStackOverflowClient(String baseUrl) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public Optional<StackOverflowResponse> processQuestionUpdates(long questionId) {
        try {
            return Optional.ofNullable(webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/questions/{id}/answers")
                    .queryParam("order", "desc")
                    .queryParam("sort", "activity")
                    .queryParam("site", "stackoverflow")
                    .build(questionId))
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

    public StackOverflowResponse parseJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode lastJsonAnswer = root.get("items").get(0);
            return objectMapper.treeToValue(lastJsonAnswer, StackOverflowResponse.class);
        } catch (JsonProcessingException e) {
            Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
            logger.error(e.getMessage());
            return null;
        }
    }
}
