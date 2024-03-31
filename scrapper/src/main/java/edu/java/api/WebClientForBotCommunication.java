package edu.java.api;

import edu.java.configuration.ApplicationConfig;
import edu.java.data.request.LinkUpdateRequest;
import edu.java.data.response.ApiErrorResponse;
import edu.java.exceptions.entities.CustomApiException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class WebClientForBotCommunication {
    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(WebClientForBotCommunication.class);
    private final ApplicationConfig.ServiceProperties serviceProperties;
    private final static int BAD_REQUEST_CODE = 400;
    private Map<Integer, Retry> retryStrategies;

    public WebClientForBotCommunication(WebClient webClient, ApplicationConfig.ServiceProperties serviceProperties) {
        this.webClient = webClient;
        this.serviceProperties = serviceProperties;
        serviceProperties.templates().forEach((code, template) -> {
            switch (template.type()) {
                case "exponential":
                    retryStrategies.put(code, Retry.backoff(template.maxAttempts(), template.delay()));
                    break;
                case "linear":
                    retryStrategies.put(code, Retry.fixedDelay(template.maxAttempts(), template.delay()));
                    break;
                case "constant":
                default:
                    retryStrategies.put(code, Retry.fixedDelay(template.maxAttempts(), template.delay()));
                    break;
            }
        });
    }

    public Optional<String> sendUpdate(LinkUpdateRequest request) {
        logger.info("Sending update to sever");
        logger.info("Amount of intrested users: " + request.tgChatIds().size());
        for (int i = 0; i < request.tgChatIds().size(); i++) {
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
                    .flatMap(errorResponse -> Mono.error(new CustomApiException(errorResponse, BAD_REQUEST_CODE)))
            )
            .bodyToMono(String.class)
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                .filter(throwable -> throwable instanceof CustomApiException)
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    throw new CustomApiException(
                        new ApiErrorResponse("Service is unavailable", String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()),"exception", "exception", new ArrayList<>()),
                        HttpStatus.SERVICE_UNAVAILABLE.value());
                }))
            .blockOptional();
    }

}
