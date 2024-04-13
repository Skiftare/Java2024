package edu.java.bot.api.web;

import edu.java.data.request.AddLinkRequest;
import edu.java.data.request.RemoveLinkRequest;
import edu.java.data.response.ApiErrorResponse;
import edu.java.data.response.LinkResponse;
import edu.java.data.response.ListLinksResponse;
import edu.java.exceptions.entities.ApiErrorException;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebClientForScrapperCommunication {
    private final static String PATH_TO_CHAT = "tg-chat/{id}";
    private final static String PATH_TO_LINK = "/links";
    private final static String HEADER_NAME = "Tg-Chat-Id";
    private final WebClient webClient;

    @Autowired
    @Qualifier("scrapperRetry")
    private Retry retry;

    public WebClientForScrapperCommunication(String incomeUrlAsBase) {
        this.webClient = WebClient.builder().baseUrl(incomeUrlAsBase).build();
    }

    public Optional<String> registerChat(Long id) {
        Mono<String> operation = webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path(PATH_TO_CHAT).build(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiErrorException(errorResponse)))
            )
            .bodyToMono(String.class);
        if (retry != null) {
            operation.transformDeferred(RetryOperator.of(retry));
        }
        return operation.blockOptional();
    }

    public Optional<String> deleteChat(Long id) {
        Mono<String> operation = webClient
            .delete()
            .uri(uriBuilder -> uriBuilder.path(PATH_TO_CHAT).build(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiErrorException(errorResponse)))
            )
            .bodyToMono(String.class);
        if (retry != null) {
            operation.transformDeferred(RetryOperator.of(retry));
        }
        return operation.blockOptional();
    }

    public Optional<ListLinksResponse> getLinks(Long id) {
        LoggerFactory.getLogger(WebClientForScrapperCommunication.class).info("Get links for chat with id: " + id);
        Mono<ListLinksResponse> operation = webClient
            .get()
            .uri(PATH_TO_LINK)
            .header(HEADER_NAME, String.valueOf(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiErrorException(errorResponse)))
            )
            .bodyToMono(ListLinksResponse.class);
        if (retry != null) {
            operation.transformDeferred(RetryOperator.of(retry));
        }
        return operation.blockOptional();
    }

    public Optional<LinkResponse> addLink(Long id, AddLinkRequest request) {
        Logger logger = LoggerFactory.getLogger(WebClientForScrapperCommunication.class);
        logger.info("Add link for chat with id: " + id + " link: " + request.link());
        Mono<LinkResponse> operation = webClient
            .post()
            .uri(PATH_TO_LINK)
            .header(HEADER_NAME, String.valueOf(id))
            .bodyValue(request)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiErrorException(errorResponse)))
            )
            .bodyToMono(LinkResponse.class);
        if (retry != null) {
            operation.transformDeferred(RetryOperator.of(retry));
        }
        return operation.blockOptional();
    }

    public Optional<LinkResponse> removeLink(Long id, RemoveLinkRequest request) {
        Mono<LinkResponse> operation = webClient.method(HttpMethod.DELETE)
            .uri(PATH_TO_LINK)
            .header(HEADER_NAME, String.valueOf(id))
            .bodyValue(request)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiErrorException(errorResponse)))
            )
            .bodyToMono(LinkResponse.class);
        if (retry != null) {
            operation.transformDeferred(RetryOperator.of(retry));
        }
        return operation.blockOptional();
    }

}
