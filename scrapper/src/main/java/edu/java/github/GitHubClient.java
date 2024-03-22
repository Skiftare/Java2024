package edu.java.github;

import java.util.Optional;

public interface GitHubClient {
    Optional<GitHubResponse> processRepositoryUpdates(String owner, String repo);
}


