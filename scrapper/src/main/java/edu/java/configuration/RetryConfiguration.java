package edu.java.configuration;

import edu.java.backoff_policy.CustomRetry;
import io.github.resilience4j.retry.Retry;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
@RequiredArgsConstructor
public class RetryConfiguration {
    private final ApplicationConfig applicationConfig;
    private final List<CustomRetry> retries;

    @Bean
    public Retry botRetry() {
        return createRetry(applicationConfig.bot());
    }

    @Bean
    public Retry stackOverflowRetry() {
        return createRetry(applicationConfig.stackoverflow());
    }

    @Bean
    public Retry gitHubRetry() {
        return createRetry(applicationConfig.github());
    }

    public Retry createRetry(ApplicationConfig.ServiceProperties serviceProperties) {
        int retryCount = serviceProperties.retryCount();
        Set<HttpStatus> httpStatuses = serviceProperties.retryCodes();
        Duration step = serviceProperties.delay();

        return retries.stream()
            .filter(e -> e.getTypeOfRetry().equals(serviceProperties.retryType()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported back off type: "))
            .createRetry(retryCount, step, httpStatuses);
    }
}
