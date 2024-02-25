package edu.java.github;

import java.util.Optional;

public interface GitHubClient {
    Optional<GitHubResponse> fetchRepositoryEvents(String owner, String repo);
    /*
        @GetExchange(url = "/repos/{owner}/{repoName}")
    GitHubRepository findRepository(@PathVariable String owner, @PathVariable String repoName);

    @GetExchange(url = "/repos/{owner}/{repoName}/activity")
    List<GitHubRepositoryActivity> findRepositoryActivities(@PathVariable String owner, @PathVariable String repoName);
     */
}


