package edu.java.configuration.retry;

import edu.java.backoff_policy.enteties.ConstantRetry;
import edu.java.backoff_policy.enteties.ExponentialRetry;
import edu.java.backoff_policy.enteties.LinearRetry;
import edu.java.configuration.ApplicationConfig;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StackoverflowRetryConfiguration {
    private final ApplicationConfig applicationConfig;
    private final ConstantRetry constantExampleRetry = new ConstantRetry();
    private final LinearRetry linearExampleRetry = new LinearRetry();
    private final ExponentialRetry exponentialExampleRetry = new ExponentialRetry();

    @Bean("stackOverflowRetry")
    @ConditionalOnProperty(value = "app.stackoverflow.retry-type", havingValue = "constant")
    public Retry stackOverflowRetryConstant() {
        return constantExampleRetry
            .createRetry(
                applicationConfig.stackoverflow().retryCount(),
                applicationConfig.stackoverflow().delay(),
                applicationConfig.stackoverflow().retryCodes()
            );
    }

    @Bean("stackOverflowRetry")
    @ConditionalOnProperty(value = "app.stackoverflow.retry-type", havingValue = "linear")
    public Retry stackOverflowRetryLinear() {
        return linearExampleRetry
            .createRetry(
                applicationConfig.stackoverflow().retryCount(),
                applicationConfig.stackoverflow().delay(),
                applicationConfig.stackoverflow().retryCodes()
            );
    }

    @Bean("stackOverflowRetry")
    @ConditionalOnProperty(value = "app.stackoverflow.retry-type", havingValue = "exponential")
    public Retry stackOverflowRetryExponential() {
        return exponentialExampleRetry
            .createRetry(
                applicationConfig.stackoverflow().retryCount(),
                applicationConfig.stackoverflow().delay(),
                applicationConfig.stackoverflow().retryCodes()
            );
    }
}
