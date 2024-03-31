package edu.java.api;

import edu.java.configuration.ApplicationConfig;
import edu.java.data.request.LinkUpdateRequest;
import edu.java.data.response.ApiErrorResponse;
import edu.java.exceptions.entities.CustomApiException;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Map<Integer, RetryTemplate> retryStrategies;

    public WebClientForBotCommunication(WebClient webClient, ApplicationConfig.ServiceProperties serviceProperties) {
        this.webClient = webClient;
        this.serviceProperties = serviceProperties;
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
                    FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
                    fixedBackOffPolicy.setBackOffPeriod(template.delay().toMillis());
                    retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
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

    public Optional<String> sendUpdate(LinkUpdateRequest request) {
        logger.info("Sending update to sever");
        logger.info("Amount of intrested users: " + request.tgChatIds().size());
        for (int i = 0; i < request.tgChatIds().size(); i++) {
            logger.info("Chat id: " + request.tgChatIds().get(i));
        }

        try {
            return executeRequest(request);
        } catch (CustomApiException ex) {
            RetryTemplate retryTemplate = retryStrategies.get(ex.getStatusCode());
            if (retryTemplate != null) {
                try {
                    return retryTemplate.execute(context -> executeRequest(request));
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
        return webClient
            .post()
            .uri("/updates")
            .bodyValue(request)
            .retrieve()
            .onStatus(
                response -> response.isError(),
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new CustomApiException(
                        errorResponse,
                        response.statusCode().value()
                    )))
            )
            .bodyToMono(String.class)
            .blockOptional();
    }

}
