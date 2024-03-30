package edu.java.api;

import edu.java.configuration.ApplicationConfig;
import edu.java.data.request.LinkUpdateRequest;
import edu.java.data.response.ApiErrorResponse;
import edu.java.exceptions.entities.CustomApiException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebClientForBotCommunication {
    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(WebClientForBotCommunication.class);

    public WebClientForBotCommunication(WebClient webClient) {
        this.webClient = webClient;
    }

    public WebClientForBotCommunication(ApplicationConfig config) {
        this.webClient = WebClient.builder().baseUrl(config.api().botUrl()).build();
    }

    public Optional<String> sendUpdate(LinkUpdateRequest request) {
        logger.info("Sending update to sever");
        logger.info("Amount of intrested users: " + request.tgChatIds().size());
        for(int i = 0;i<request.tgChatIds().size();i++){
            logger.info("Chat id: " + request.tgChatIds().get(i));
        }
        return webClient
            .post()
            .uri("/updates")
            .bodyValue(request)
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new CustomApiException(errorResponse)))
            )
            .bodyToMono(String.class)
            .blockOptional();
    }
}
