package edu.java.scrapper.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.data.request.LinkUpdateRequest;
import edu.java.data.response.ApiErrorResponse;
import edu.java.exceptions.entities.CustomApiException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

public class WebServerTest {
    private WebClientForBotCommunication client;
    private static final WireMockServer mockedServer = new WireMockServer();

    @BeforeEach
    void setUpEnvironment() {
        mockedServer.start();
        String baseUrl = "http://localhost:" + mockedServer.port();
        client = new WebClientForBotCommunication(WebClient.create(baseUrl), null);
    }

    @AfterEach
    void resetEnvironment() {
        mockedServer.stop();
    }

    @Test
    @DisplayName("/updates: POST; Correct result")
    public void testThatPostCorrectUpdatesResponseAndReturnedExpectedSuccessResult() throws URISyntaxException {
        String expectedResponseBody = "Обновление обработано";
        LinkUpdateRequest requestToClient = new LinkUpdateRequest(
            new URI("1"),
            "1",
            List.of(1L)
        );

        mockedServer.stubFor(post(urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(expectedResponseBody)));

        Optional<String> actualResponse = client.sendUpdate(requestToClient);

        assertThat(actualResponse).isPresent();
        assertThat(actualResponse.get()).isEqualTo(expectedResponseBody);
    }

    @Test
    @DisplayName("/updates: POST; Incorrect result")
    public void testThatPostIncorrectUpdatesResponseAndReturnedExpectedSuccessResult() throws URISyntaxException {
        String expectedResponseBody = """
                {
                    "description":"123",
                    "code":"400",
                    "exceptionName":"123",
                    "exceptionMessage":"123",
                    "stackTrace":[
                        "1",
                        "2",
                        "3"
                    ]
                }
            """;

        LinkUpdateRequest requestToClient = new LinkUpdateRequest(
            new URI("1"),
            "1",
            List.of(1L)
        );

        String expectedDescription = "123";
        String expectedCode = "400";
        String expectedName = "123";
        String expectedMessage = "123";

        mockedServer.stubFor(post(urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(expectedResponseBody)));

        CustomApiException thrownException = catchThrowableOfType(
            () -> client.sendUpdate(requestToClient),
            CustomApiException.class
        );
        ApiErrorResponse actualResponse = thrownException.getErrorResponse();

        assertThat(actualResponse.description()).isEqualTo(expectedDescription);
        assertThat(actualResponse.code()).isEqualTo(expectedCode);
        assertThat(actualResponse.exceptionName()).isEqualTo(expectedName);
        assertThat(actualResponse.exceptionMessage()).isEqualTo(expectedMessage);
        assertThat(actualResponse.exceptionMessage()).isEqualTo(expectedMessage);

    }

}
