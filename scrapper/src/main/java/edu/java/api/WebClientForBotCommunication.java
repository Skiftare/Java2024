package edu.java.api;

import edu.java.backoff_policy.LinearBackOffPolicy;
import edu.java.configuration.ApplicationConfig;
import edu.java.data.request.LinkUpdateRequest;
import edu.java.data.response.ApiErrorResponse;
import edu.java.exceptions.entities.ApiErrorException;
import java.util.HashMap;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebClientForBotCommunication {
    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(WebClientForBotCommunication.class);
    private final ApplicationConfig.ServiceProperties serviceProperties;
    private final static int BAD_REQUEST_CODE = 400;
    private HashMap<Integer, RetryTemplate> retryStrategies = new HashMap<>();

    public WebClientForBotCommunication(WebClient webClient, ApplicationConfig.ServiceProperties serviceProperties) {
        this.webClient = webClient;
        this.serviceProperties = serviceProperties;
        initRetryStrategies();
    }

    public Optional<String> sendUpdate(LinkUpdateRequest request) {
        logger.info("Sending update to sever");
        logger.info("Amount of intrested users: " + request.tgChatIds().size());
        for (int i = 0; i < request.tgChatIds().size(); i++) {
            logger.info("Chat id: " + request.tgChatIds().get(i));
        }

        try {
            return executeRequest(request);
        } catch (ApiErrorException ex) {
            logger.error("Error occurred with code", ex.getErrorResponse().code());
            RetryTemplate retryTemplate = retryStrategies.get(Integer.getInteger(ex.getErrorResponse().code()));
            if (retryTemplate != null) {
                try {
                    return retryTemplate.execute(_ -> executeRequest(request));
                } catch (Exception retryEx) {
                    logger.error("All retry attempts failed", retryEx);
                    return Optional.empty();
                }
            } else {
                logger.error("Unexpected error occurred", ex);
                return Optional.empty();
            }
        }

    }

    private Optional<String> executeRequest(LinkUpdateRequest request) {
        logger.info("Sending request to server: " + request.toString());
        logger.info(request.description() + "\n" + request.url() + "\n" + request.tgChatIds().size());
        return webClient
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
            .bodyToMono(String.class)
            .blockOptional();
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
