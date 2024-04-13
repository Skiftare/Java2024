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
public class BotRetryConfiguration {
    private final ApplicationConfig applicationConfig;
    private final ConstantRetry constantExampleRetry = new ConstantRetry();
    private final LinearRetry linearExampleRetry = new LinearRetry();
    private final ExponentialRetry exponentialExampleRetry = new ExponentialRetry();

    @Bean("botRetry")
    @ConditionalOnProperty(value = "app.bot.retry-type", havingValue = "constant")
    public Retry botRetryConstant() {
        return constantExampleRetry
            .createRetry(
                applicationConfig.bot().retryCount(),
                applicationConfig.bot().delay(),
                applicationConfig.bot().retryCodes()
            );
    }

    @Bean("botRetry")
    @ConditionalOnProperty(value = "app.bot.retry-type", havingValue = "linear")
    public Retry botRetryLinear() {
        return linearExampleRetry
            .createRetry(
                applicationConfig.bot().retryCount(),
                applicationConfig.bot().delay(),
                applicationConfig.bot().retryCodes()
            );
    }

    @Bean("botRetry")
    @ConditionalOnProperty(value = "app.bot.retry-type", havingValue = "exponential")
    public Retry botRetryExponential() {
        return exponentialExampleRetry
            .createRetry(
                applicationConfig.bot().retryCount(),
                applicationConfig.bot().delay(),
                applicationConfig.bot().retryCodes()
            );
    }
}
