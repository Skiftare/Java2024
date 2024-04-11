package edu.java.bot.api.backoff_policy.enteties;

import edu.java.bot.api.backoff_policy.CustomRetry;
import edu.java.bot.api.backoff_policy.RetryType;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
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
