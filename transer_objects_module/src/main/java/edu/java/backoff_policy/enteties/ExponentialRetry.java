package edu.java.backoff_policy.enteties;

import edu.java.backoff_policy.CustomRetry;
import edu.java.backoff_policy.RetryType;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.time.Duration;
import java.util.Set;

public class ExponentialRetry implements CustomRetry {

    @Override
    public RetryConfig getRetryConfig(int retryCount, Duration step, Set<HttpStatus> httpStatuses) {
        return RetryConfig.<WebClientResponseException>custom()
            .maxAttempts(retryCount)
            .intervalFunction(IntervalFunction.ofExponentialBackoff(step))
            .retryOnException(e -> e instanceof WebClientResponseException
                && httpStatuses.contains(HttpStatus.resolve(((WebClientResponseException) e).getStatusCode().value())))
            .build();
    }

    @Override
    public RetryType getTypeOfRetry() {
        return RetryType.EXPONENTIAL;
    }
}
