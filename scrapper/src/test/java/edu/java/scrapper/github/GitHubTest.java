package edu.java.scrapper.github;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.links_clients.github.DefaultGitHubClient;
import edu.java.links_clients.github.GitHubClient;
import edu.java.links_clients.github.GitHubResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GitHubTest {
    private WireMockServer wireMockServer;
    private GitHubClient gitHubClient;

    @BeforeEach
    void setUpMockedServer() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
        gitHubClient = new DefaultGitHubClient(STR."http://localhost:\{wireMockServer.port()}", null);
    }

    @AfterEach
    void shutDownMockedServer() {
        wireMockServer.stop();
    }

    @Test
    public void testThatGetRealGithubDataAndReturnCorrectInfoAfterParsing() throws IOException {

        //Given: real github json data
        String ownerName = "Skiftare";
        String repoName = "Java2023";

        String responseBody = new String(Files.readAllBytes(Path.of("src/test/resources/githubRawJson.json")));

        Long expectedId = 34318456632L;
        String expectedType = "PushEvent";
        String expectedActorName = "Skiftare";
        String expectedRepoName = "Skiftare/Java2023";
        OffsetDateTime expectedCreatedAt = OffsetDateTime.parse("2023-12-20T18:11:12Z");

        UriComponentsBuilder uri = UriComponentsBuilder
            .fromPath("/repos/{owner}/{repo}/events")
            .queryParam("per_page", Collections.singleton(1))
            .uriVariables(Map.of(
                "owner", ownerName,
                "repo", repoName
            ));
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(uri.toUriString()))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)
            )
        );

        //When: we process this data by GitHub client
        GitHubResponse response = gitHubClient.processRepositoryUpdates(ownerName, repoName).orElse(null);

        //Then: we get real&expected data, not phantom-generated
        assertThat(response).isNotNull();
        assert response != null;
        assertThat(expectedId).isEqualTo(response.id());
        assertThat(expectedType).isEqualTo(response.type());
        assertThat(expectedActorName).isEqualTo(response.actor().login());
        assertThat(expectedRepoName).isEqualTo(response.repo().name());
        assertThat(expectedCreatedAt).isEqualTo(response.createdAt());
    }

    @Test
    public void testThatGetEmptyBodyAndReturnedExceptionMessageAndNullResult() {

        //Given:empty response body
        String ownerName = "Skiftare";
        String repoName = "Java2024";
        String responseBody = "[]";
        UriComponentsBuilder uri = UriComponentsBuilder
            .fromPath("/repos/{owner}/{repo}/events")
            .queryParam("per_page", Collections.singleton(1))
            .uriVariables(Map.of(
                "owner", ownerName,
                "repo", repoName
            ));
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(uri.toUriString()))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)
            )
        );

        //When: we process this data by GitHub client
        Optional<GitHubResponse> response = gitHubClient.processRepositoryUpdates(ownerName, repoName);

        //Then: we get some log errors and Optional.empty() as a result
        assertThat(response).isEmpty();
        assertThat(response).isNotPresent();

    }

    @Test
    public void testThatGetIncorrectDataAndReturnExceptionMessageAndNullResult() {

        //Given: invalid, not-json data
        String ownerName = "Skiftare";
        String repoName = "Skiftare/Java2023";
        String responseBody = "this is not correct data at all";
        UriComponentsBuilder uri = UriComponentsBuilder
            .fromPath("/repos/{owner}/{repo}/events")
            .queryParam("per_page", Collections.singleton(1))
            .uriVariables(Map.of(
                "owner", ownerName,
                "repo", repoName
            ));
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(uri.toUriString()))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)
            )
        );

        //When: we process this data by GitHub client
        Optional<GitHubResponse> response = gitHubClient.processRepositoryUpdates(ownerName, repoName);

        //Then: we get some log errors and Optional.empty() as a result
        assertThat(response).isNotPresent();
        assertThat(response).isEmpty();
    }

}
