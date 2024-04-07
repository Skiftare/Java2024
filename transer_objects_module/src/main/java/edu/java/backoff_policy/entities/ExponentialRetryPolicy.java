package edu.java.backoff_policy.entities;

import edu.java.backoff_policy.RetryLogicType;
import edu.java.backoff_policy.RetryPolicy;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class ExponentialRetryPolicy implements RetryPolicy {

    @Override
    public RetryLogicType getRetryLogicType() {
        return RetryLogicType.EXPONENTIAL;
    }

    @Override
    public RetryConfig getRetryConfig(int retryCount, Duration step, Set<HttpStatus> httpStatuses) {
        return RetryConfig.<WebClientResponseException>custom()
            .maxAttempts(retryCount)
            .intervalFunction(IntervalFunction.ofExponentialBackoff(step))
            .retryOnException(e -> e instanceof WebClientResponseException
                && httpStatuses.contains(HttpStatus.resolve(((WebClientResponseException) e).getStatusCode().value())))
            .build();
    }
}
