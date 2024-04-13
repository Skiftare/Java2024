package edu.java.api;

import edu.java.data.request.LinkUpdateRequest;
import edu.java.data.response.ApiErrorResponse;
import edu.java.exceptions.entities.ApiErrorException;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebClientForBotCommunication {
    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(WebClientForBotCommunication.class);

    private final Retry retry;

    public WebClientForBotCommunication(WebClient webClient, Retry retry) {
        this.webClient = webClient;
        this.retry = retry;
    }

    public Optional<String> sendUpdate(LinkUpdateRequest request) {
        logger.info("Sending update to sever");
        logger.info("Amount of intrested users: " + request.tgChatIds().size());
        for (int i = 0; i < request.tgChatIds().size(); i++) {
            logger.info("Chat id: " + request.tgChatIds().get(i));
        }
        Mono<String> operation = webClient
            .post()
            .uri("/updates")
            .header("header")
            .bodyValue(request)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiErrorException(errorResponse)))
            )
            .bodyToMono(String.class);

        if (retry != null) {
            operation = operation.transformDeferred(RetryOperator.of(retry));
        }

        return operation.blockOptional();

    }

}
