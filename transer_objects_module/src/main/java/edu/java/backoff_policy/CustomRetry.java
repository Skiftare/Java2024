package edu.java.backoff_policy;

import org.springframework.http.HttpStatus;
import java.time.Duration;
import java.util.Set;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

public interface CustomRetry {
    default Retry createRetry(int retryCount, Duration step, Set<HttpStatus> httpStatuses) {
        return Retry.of(getTypeOfRetry().name(), getRetryConfig(retryCount, step, httpStatuses));
    }

    RetryType getTypeOfRetry();

    RetryConfig getRetryConfig(int retryCount, Duration step, Set<HttpStatus> httpStatuses);

}
