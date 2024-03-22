package edu.java.links_clients.github;

import java.util.Optional;

public interface GitHubClient {
    Optional<GitHubResponse> processRepositoryUpdates(String owner, String repo);
}


