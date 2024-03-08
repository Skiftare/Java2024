package edu.java.bot.processor;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.bot.api.entities.exceptions.ApiErrorException;
import edu.java.bot.api.entities.requests.AddLinkRequest;
import edu.java.bot.api.entities.requests.RemoveLinkRequest;
import edu.java.bot.api.entities.responses.LinkResponse;
import edu.java.bot.api.entities.responses.ListLinksResponse;
import edu.java.bot.api.web.WebClientForScrapperCommunication;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.assertj.core.api.ThrowableAssert.catchThrowableOfType;

public class ScrapperClientTest {
    private WebClientForScrapperCommunication scrapperWebClient;
    private static WireMockServer wireMockServer = new WireMockServer();

    private final static String INVALID_BODY = """
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

    private final static String NOT_FOUND_BODY = """
            {
                "description":"123",
                "code":"404",
                "exceptionName":"123",
                "exceptionMessage":"123",
                "stackTrace":[
                    "1",
                    "2",
                    "3"
                ]
            }
        """;

    private final static String LINK_BODY = """
        {
            "id":1,
            "url":"123"
        }
        """;

    @BeforeEach
    void setUp() {
        wireMockServer.start();
        String baseUrl = "http://localhost:" + wireMockServer.port();
        scrapperWebClient = new WebClientForScrapperCommunication(baseUrl);
    }

    @Test
    public void testThatGetCorrectPostRequestToRegisterChatForTheFirstTimeAndReturnedSuccessRegistration() {
        // Arrange
        String responseBody = "Чат зарегистрирован";
        wireMockServer.stubFor(post(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)));

        // Act
        Optional<String> actualResponse = scrapperWebClient.registerChat(1L);

        // Assert
        assertThat(actualResponse).isPresent();
        assertThat(actualResponse.get()).isEqualTo(responseBody);
    }

    @Test
    public void testThatCorrectGetPostRequestToRegisterChatTwiceAndReturnedExceptionAtTheSecondRegistration() {
        // Arrange
        String responseBody = "Чат зарегистрирован";
        wireMockServer.stubFor(post(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)));

        scrapperWebClient.registerChat(1L);

        wireMockServer.stubFor(post(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(INVALID_BODY)));

        // Act
        Throwable actualException = catchThrowableOfType(
            () -> scrapperWebClient.registerChat(1L),
            ApiErrorException.class
        );

        // Assert
        assertThat(actualException)
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    public void testThatCorrectGetDeleteRequestToRemoveTheChatWhichIsRegisteredAndReturnedSuccessRemoving() {
        // Arrange
        String responseBody = "Чат успешно удалён";
        wireMockServer.stubFor(delete(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)));

        // Act
        Optional<String> actualResponse = scrapperWebClient.deleteChat(1L);

        // Assert
        assertThat(actualResponse).isPresent();
        assertThat(actualResponse.get()).isEqualTo(responseBody);
    }

    @Test
    public void testThatCorrectGetDeleteRequestToRemoveTheChatWhichIsNotRegisteredAndReturnedFailureExceptionChatNotFound() {
        // Arrange
        wireMockServer.stubFor(delete(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(NOT_FOUND_BODY)));

        // Act
        Throwable actualException = catchThrowableOfType(
            () -> scrapperWebClient.deleteChat(1L),
            ApiErrorException.class
        );

        // Assert
        assertThat(actualException)
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    public void testThatGetCorrectGetRequestToClaimLinksForUserFromTheDatabaseAndReturnedExpectedListOfLinks() {
        // Arrange
        String responseBody = """
            {
                "links":[
                    {
                        "id":1,
                        "url":"link"
                    }
                ],
                "size":1
            }
            """;
        wireMockServer.stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)));

        // Act
        Optional<ListLinksResponse> actualResponse = scrapperWebClient.getLinks(1L);

        // Assert
        assertThat(actualResponse).isPresent();
        assertThat(actualResponse.get().size()).isEqualTo(1);
        assertThat(actualResponse.get().links())
            .hasSize(1)
            .extracting(LinkResponse::id, link -> link.url().getPath())
            .containsExactly(tuple(1L, "link"));
    }

    @Test
    public void testThatGetIncorrectGetRequestToClaimLinksForUserFromDatabaseAndReturnedExceptionOfInvalidId() {
        // Arrange
        wireMockServer.stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("-1"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(INVALID_BODY)));

        // Act
        Throwable actualException = catchThrowableOfType(
            () -> scrapperWebClient.getLinks(-1L),
            ApiErrorException.class
        );

        // Assert
        assertThat(actualException)
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    public void testThatGetCorrectGetRequestToClaimLinksForUserFromTheEmptyDatabaseAndReturnedExceptionOfNoLinksAreStored() {
        // Arrange
        wireMockServer.stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(NOT_FOUND_BODY)));

        // Act
        Throwable actualException = catchThrowableOfType(
            () -> scrapperWebClient.getLinks(1L),
            ApiErrorException.class
        );

        // Assert
        assertThat(actualException)
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    public void testTHatGetCorrectPostRequestToRegisterNewLinkForUserAndReturnedSuccessRegistration() throws URISyntaxException {
        // Arrange
        wireMockServer.stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(LINK_BODY)));

        // Act
        Optional<LinkResponse> actualResponse = scrapperWebClient.addLink(
            1L, new AddLinkRequest(new URI("123"))
        );

        // Assert
        assertThat(actualResponse).isPresent();
        assertThat(actualResponse.get().id()).isEqualTo(1);
        assertThat(actualResponse.get().url().getPath()).isEqualTo("123");
    }

    @Test
    public void testTHatGetIncorrectPostRequestToRegisterNewLinkForUserAndReturnedExceptionOfInvalidId() {
        // Arrange
        wireMockServer.stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("-1"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(INVALID_BODY)));

        // Act
        Throwable actualException = catchThrowableOfType(
            () -> scrapperWebClient.addLink(-1L, new AddLinkRequest(new URI("123"))),
            ApiErrorException.class
        );

        // Assert
        assertThat(actualException)
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    public void testThatGetIncorrectPostRequestToRegisterNewLinkForUserAndReturnedExceptionOfInvalidBody() {
        // Arrange
        wireMockServer.stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(INVALID_BODY)));

        // Act
        Throwable actualException = catchThrowableOfType(
            () -> scrapperWebClient.addLink(1L, new AddLinkRequest(new URI(""))),
            ApiErrorException.class
        );

        // Assert
        assertThat(actualException)
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    public void testThatGetIncorrectPostRequestToRegisterNewLinkForUserAndReturnedExceptionOfNotFoundBody() {
        // Arrange
        wireMockServer.stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(NOT_FOUND_BODY)));

        // Act
        Throwable actualException = catchThrowableOfType(
            () -> scrapperWebClient.addLink(1L, new AddLinkRequest(new URI("123"))),
            ApiErrorException.class
        );

        // Assert
        assertThat(actualException)
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    public void testThatGetCorrectDeleteRequestToRemoveTheLinkAndReturnedSuccessDeleting() throws URISyntaxException {
        // Arrange
        wireMockServer.stubFor(delete(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(LINK_BODY)));

        // Act
        Optional<LinkResponse> actualResponse = scrapperWebClient.removeLink(
            1L, new RemoveLinkRequest(new URI("123"))
        );

        // Assert
        assertThat(actualResponse).isPresent();
        assertThat(actualResponse.get().id()).isEqualTo(1);
        assertThat(actualResponse.get().url().getPath()).isEqualTo("123");
    }

    @Test
    public void testThatGetIncorrectDeleteRequestToRemoveTheLinkAndReturnedExceptionOfInvalidId() {
        // Arrange
        wireMockServer.stubFor(delete(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("-1"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(INVALID_BODY)));

        // Act
        Throwable actualException = catchThrowableOfType(
            () -> scrapperWebClient.removeLink(-1L, new RemoveLinkRequest(new URI("123"))),
            ApiErrorException.class
        );

        // Assert
        assertThat(actualException)
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    public void testThatGetIncorrectDeleteRequestToRemoveTheLinkAndReturnedExceptionOfInvalidBody() {
        // Arrange
        wireMockServer.stubFor(delete(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(INVALID_BODY)));

        // Act
        Throwable actualException = catchThrowableOfType(
            () -> scrapperWebClient.removeLink(1L, new RemoveLinkRequest(new URI(""))),
            ApiErrorException.class
        );

        // Assert
        assertThat(actualException)
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    public void testThatGetIncorrectDeleteRequestToRemoveTheLinkAndReturnedExceptionOfLinkWasNotFound() {
        // Arrange
        wireMockServer.stubFor(delete(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(NOT_FOUND_BODY)));

        // Act
        Throwable actualException = catchThrowableOfType(
            () -> scrapperWebClient.removeLink(1L, new RemoveLinkRequest(new URI("123"))),
            ApiErrorException.class
        );

        // Assert
        assertThat(actualException)
            .isInstanceOf(ApiErrorException.class);
    }
}
