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
public class GithubRetryConfiguration {
    private final ApplicationConfig applicationConfig;
    private final ConstantRetry constantExampleRetry = new ConstantRetry();
    private final LinearRetry linearExampleRetry = new LinearRetry();
    private final ExponentialRetry exponentialExampleRetry = new ExponentialRetry();

    @Bean("gitHubRetry")
    @ConditionalOnProperty(value = "app.github.retry-type", havingValue = "constant")
    public Retry gitHubRetryConstant() {
        return constantExampleRetry
            .createRetry(
                applicationConfig.github().retryCount(),
                applicationConfig.github().delay(),
                applicationConfig.github().retryCodes()
            );

    }

    @Bean("gitHubRetry")
    @ConditionalOnProperty(value = "app.github.retry-type", havingValue = "linear")
    public Retry gitHubRetryLinear() {
        return linearExampleRetry
            .createRetry(
                applicationConfig.github().retryCount(),
                applicationConfig.github().delay(),
                applicationConfig.github().retryCodes()
            );
    }

    @Bean("gitHubRetry")
    @ConditionalOnProperty(value = "app.github.retry-type", havingValue = "exponential")
    public Retry gitHubRetryExponential() {
        return exponentialExampleRetry
            .createRetry(
                applicationConfig.github().retryCount(),
                applicationConfig.github().delay(),
                applicationConfig.github().retryCodes()
            );
    }
}
