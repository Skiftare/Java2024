package edu.java.configuration;

import edu.java.github.DefaultGitHubClient;
import edu.java.github.GitHubClient;
import edu.java.stackoverflow.DefaultStackOverflowClient;
import edu.java.stackoverflow.StackOverflowClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    private final ApplicationConfig applicationConfig;

    @Autowired
    public ClientConfiguration(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean
    public StackOverflowClient webStackOverflowClient() {
        return new DefaultStackOverflowClient(applicationConfig);
    }

    @Bean
    public GitHubClient webGitHubClient() {
        return new DefaultGitHubClient(applicationConfig);

    }
}
