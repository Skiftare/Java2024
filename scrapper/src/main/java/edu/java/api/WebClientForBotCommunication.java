package edu.java.api;

import edu.java.data.request.LinkUpdateRequest;
import edu.java.data.response.ApiErrorResponse;
import edu.java.exceptions.entities.CustomApiException;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebClientForBotCommunication {
    private final WebClient webClient;

    public WebClientForBotCommunication(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public Optional<String> sendUpdate(LinkUpdateRequest request) {
        return webClient
            .post()
            .uri("/updates")
            .body(BodyInserters.fromValue(request))
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
