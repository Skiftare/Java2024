package edu.java.configuration;

import edu.java.github.DefaultGitHubClient;
import edu.java.github.GitHubClient;
import edu.java.stackoverflow.DefaultStackOverflowClient;
import edu.java.stackoverflow.StackOverflowClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfiguration {
    private static final String GITHUB_COM = "https://api.github.com/";
    private static final String STACKOVERFLOW = "https://api.stackexchange.com/";

    private final ApplicationConfig applicationConfig;

    @Autowired
    public ClientConfiguration(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean
    public StackOverflowClient WebStackOverflowClient () {
        return new DefaultStackOverflowClient();
    }

    @Bean
    public GitHubClient WebGitHubClient () {
        return new DefaultGitHubClient();

    }
}
