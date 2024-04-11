package edu.java.bot.configuration;

import edu.java.bot.api.backoff_policy.RetryType;
import jakarta.validation.constraints.NotEmpty;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    Api api,
    ServiceProperties scrapper
) {
    public record Api(String scrapperUrl) {

    }

    public record ServiceProperties(
        RetryType retryType,
        int retryCount,
        Duration delay,
        Set<HttpStatus> retryCodes
    ) {
    }
}
