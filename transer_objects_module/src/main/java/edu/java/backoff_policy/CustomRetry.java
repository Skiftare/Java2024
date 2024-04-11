package edu.java.backoff_policy;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.Set;
import org.springframework.http.HttpStatus;

public interface CustomRetry {
    default Retry createRetry(int retryCount, Duration step, Set<HttpStatus> httpStatuses) {
        return Retry.of(getTypeOfRetry().name(), getRetryConfig(retryCount, step, httpStatuses));
    }

    RetryType getTypeOfRetry();

    RetryConfig getRetryConfig(int retryCount, Duration step, Set<HttpStatus> httpStatuses);

}
