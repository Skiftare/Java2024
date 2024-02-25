package edu.java.scrapper.stackoverflow;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.stackoverflow.DefaultStackOverflowClient;
import edu.java.stackoverflow.StackOverflowClient;
import edu.java.stackoverflow.StackOverflowResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class StackOverflowTest {
    private WireMockServer wireMockServer;
    private StackOverflowClient stackOverflowClient;

    @BeforeEach
    void setUpMockedServer() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
        stackOverflowClient = new DefaultStackOverflowClient(STR."http://localhost:\{wireMockServer.port()}");
    }

    @AfterEach
    void shutDownMockedServer() {
        wireMockServer.stop();
    }

    @Test
    public void testThatGetRealStackOverflowDataAndReturnCorrectInfoAfterParsing() throws IOException {
        long questionId = 21295883L;
        String responseBody = new String(Files.readAllBytes(Path.of("src/test/resources/stackOverflowRawJson.json")));

        OffsetDateTime expectedLastActivityDate = Instant.ofEpochSecond(1708878710L).atOffset(ZoneOffset.UTC);
        Long expectedQuestionId = 78056645L;
        Long expectedAnswerId = 0L;
        String expectedOwnerName = "khw";
        var uri = UriComponentsBuilder
            .fromPath("/questions/{id}/answers")
            .queryParam("order", "desc")
            .queryParam("sort", "activity")
            .queryParam("site", "stackoverflow")
            .uriVariables(Map.of("id", questionId));
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(uri.toUriString()))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)
            )
        );

        StackOverflowResponse response = stackOverflowClient.processQuestionUpdates(questionId).orElse(null);

        assertThat(response).isNotNull();
        assert response != null;
        assertThat(expectedQuestionId).isEqualTo(response.questionId());
        assertThat(expectedAnswerId).isEqualTo(response.answerId());
        assertThat(expectedLastActivityDate).isEqualTo(response.lastActivityDate());
        assertThat(expectedOwnerName).isEqualTo(response.owner().displayName());

    }

    @Test
    public void testThatGetEmptyStackOverflowDataAndReturnNullObjectWithExceptionMessage() {
        long questionId = 78056645L;
        String responseBody = "{}";
        var uri = UriComponentsBuilder
            .fromPath("/questions/{id}/answers")
            .queryParam("order", "desc")
            .queryParam("sort", "activity")
            .queryParam("site", "stackoverflow")
            .uriVariables(Map.of("id", questionId));
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(uri.toUriString()))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)
            )
        );

        Optional<StackOverflowResponse> response = stackOverflowClient.processQuestionUpdates(questionId);

        assertThat(response).isEmpty();
        assertThat(response).isNotPresent();
    }

    @Test
    public void testThatGetInvalidStackOverflowDataAndReturnNullObjectWithExceptionMessage() {
        long questionId = 78056645L;
        String responseBody = "this is not correct data at all too";

        var uri = UriComponentsBuilder
            .fromPath("/questions/{id}/answers")
            .queryParam("order", "desc")
            .queryParam("sort", "activity")
            .queryParam("site", "stackoverflow")
            .uriVariables(Map.of("id", questionId));
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(uri.toUriString()))
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)
            )
        );

        var response = stackOverflowClient.processQuestionUpdates(questionId);

        assertThat(response).isEmpty();
        assertThat(response).isNotPresent();
    }
}

