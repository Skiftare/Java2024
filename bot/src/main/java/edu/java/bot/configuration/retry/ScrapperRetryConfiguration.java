package edu.java.bot.configuration.retry;

import edu.java.backoff_policy.enteties.ConstantRetry;
import edu.java.backoff_policy.enteties.ExponentialRetry;
import edu.java.backoff_policy.enteties.LinearRetry;
import edu.java.bot.configuration.ApplicationConfig;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ScrapperRetryConfiguration {
    private final ApplicationConfig applicationConfig;
    private final ConstantRetry constantExampleRetry = new ConstantRetry();
    private final LinearRetry linearExampleRetry = new LinearRetry();
    private final ExponentialRetry exponentialExampleRetry = new ExponentialRetry();

    @Bean("scrapperRetry")
    @ConditionalOnProperty(value = "app.scrapper.retry-type", havingValue = "constant")
    public Retry scrapperRetryConstant() {
        return constantExampleRetry
            .createRetry(
                applicationConfig.scrapper().retryCount(),
                applicationConfig.scrapper().delay(),
                applicationConfig.scrapper().retryCodes()
            );
    }

    @Bean("scrapperRetry")
    @ConditionalOnProperty(value = "app.scrapper.retry-type", havingValue = "linear")
    public Retry scrapperRetryLinear() {
        return linearExampleRetry
            .createRetry(
                applicationConfig.scrapper().retryCount(),
                applicationConfig.scrapper().delay(),
                applicationConfig.scrapper().retryCodes()
            );
    }

    @Bean("scrapperRetry")
    @ConditionalOnProperty(value = "app.scrapper.retry-type", havingValue = "exponential")
    public Retry scrapperRetryExponential() {
        return exponentialExampleRetry
            .createRetry(
                applicationConfig.scrapper().retryCount(),
                applicationConfig.scrapper().delay(),
                applicationConfig.scrapper().retryCodes()
            );
    }
}
