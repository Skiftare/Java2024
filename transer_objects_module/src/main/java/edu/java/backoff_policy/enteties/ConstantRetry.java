package edu.java.backoff_policy.enteties;

import edu.java.backoff_policy.CustomRetry;
import edu.java.backoff_policy.RetryType;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class ConstantRetry implements CustomRetry {

    @Override
    public RetryConfig getRetryConfig(int retryCount, Duration step, Set<HttpStatus> httpStatuses) {
        return RetryConfig.<WebClientResponseException>custom()
            .maxAttempts(retryCount)
            .waitDuration(step)
            .retryOnException(e -> e instanceof WebClientResponseException
                && httpStatuses.contains(HttpStatus.resolve(((WebClientResponseException) e).getStatusCode().value())))
            .build();
    }

    @Override
    public RetryType getTypeOfRetry() {
        return RetryType.CONSTANT;
    }
}
