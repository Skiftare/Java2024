package edu.java.links_clients;

import edu.java.configuration.ApplicationConfig;
import edu.java.links_clients.dto.github.GithubActions;
import edu.java.links_clients.dto.stckoverflow.AnswerInfo;
import edu.java.links_clients.dto.stckoverflow.CommentInfo;
import edu.java.links_clients.github.DefaultGitHubClient;
import edu.java.links_clients.stackoverflow.DefaultStackOverflowClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LinkHandler {
    private static final String URL = "url: ";
    private static final String SPLIT_URL = "/";
    private final ApplicationConfig.ListOfSupportedLinks listOfSupprotedLinks;
    private final DefaultStackOverflowClient stackOverflowClient;
    private final DefaultGitHubClient gitHubService;

    public LinkHandler(
        ApplicationConfig appConf,
        DefaultStackOverflowClient stackOverflowClient,
        DefaultGitHubClient gitHubClient
    ) {
        this.listOfSupprotedLinks = appConf.listOfLinksSupported();
        this.stackOverflowClient = stackOverflowClient;
        this.gitHubService = gitHubClient;
    }

    public boolean isGitHubUrl(String url) {
        return url.contains(listOfSupprotedLinks.github());
    }

    public boolean isStackOverflowUrl(String url) {
        return url.contains(listOfSupprotedLinks.stackoverflow());
    }

    public List<GithubActions> getActionsGitHubInfoByUrl(String url) {
        String[] urlParts = url.split(SPLIT_URL);
        String owner = urlParts[urlParts.length - 2];
        String repo = urlParts[urlParts.length - 1];
        return gitHubService.getActionsInfo(owner, repo);
    }

    public List<AnswerInfo> getAnswersStackOverflowByUrl(String url) {
        String[] urlParts = url.split(SPLIT_URL);
        Long question = Long.valueOf(urlParts[urlParts.length - 2]);
        return stackOverflowClient.getAnswerInfoByQuestion(question);
    }

    public List<CommentInfo> getCommentsStackOverflowByUrl(String url) {
        String[] urlParts = url.split(SPLIT_URL);
        Long question = Long.valueOf(urlParts[urlParts.length - 2]);
        return stackOverflowClient.getCommentInfoByQuestion(question);
    }

}
