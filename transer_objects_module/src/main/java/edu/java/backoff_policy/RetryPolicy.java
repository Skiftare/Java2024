package edu.java.backoff_policy;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.Set;
import org.springframework.http.HttpStatus;

public interface RetryPolicy {
    default Retry createPolicy(int retryCount, Duration step, Set<HttpStatus> httpStatuses) {
        return Retry.of(getRetryLogicType().name(), getRetryConfig(retryCount, step, httpStatuses));
    }

    RetryLogicType getRetryLogicType();

    RetryConfig getRetryConfig(int retryCount, Duration step, Set<HttpStatus> httpStatuses);

}
