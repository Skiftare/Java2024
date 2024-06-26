package edu.java.backoff_policy.enteties;

import edu.java.backoff_policy.CustomRetry;
import edu.java.backoff_policy.RetryType;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class LinearRetry implements CustomRetry {
    @Override
    public RetryType getTypeOfRetry() {
        return RetryType.LINEAR;
    }

    @Override
    public RetryConfig getRetryConfig(int retryCount, Duration step, Set<HttpStatus> httpStatuses) {
        long interval = step.toSeconds();
        return RetryConfig.<WebClientResponseException>custom()
            .maxAttempts(retryCount)
            .intervalFunction(attempt -> interval * attempt)
            .retryOnException(e -> e instanceof WebClientResponseException
                && httpStatuses.contains(HttpStatus.resolve(((WebClientResponseException) e).getStatusCode().value())))
            .build();
    }
}
