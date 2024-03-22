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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Nested
    class TgChatTests {
        @Nested
        class PostRequests {
            @Test
            @DisplayName("/tg-chat/: POST; Correct result")
            public void testThatGetCorrectPostRequestToRegisterChatForTheFirstTimeAndReturnedSuccessRegistration() {

                String responseBody = "Чат зарегистрирован";
                wireMockServer.stubFor(post(urlEqualTo("/tg-chat/1"))
                    .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(responseBody)));

                Optional<String> actualResponse = scrapperWebClient.registerChat(1L);

                assertThat(actualResponse).isPresent();
                assertThat(actualResponse.get()).isEqualTo(responseBody);
            }

            @Test
            @DisplayName("/tg-chat/: POST; Incorrect result - double registration")
            public void testThatCorrectGetPostRequestToRegisterChatTwiceAndReturnedExceptionAtTheSecondRegistration() {
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

                Throwable actualException = catchThrowableOfType(
                    () -> scrapperWebClient.registerChat(1L),
                    ApiErrorException.class
                );

                assertThat(actualException)
                    .isInstanceOf(ApiErrorException.class);
            }
        }

        @Nested
        class GetRequests {

        }

        @Nested
        class DeleteRequests {
            @Test
            @DisplayName("/tg-chat/: DELETE; Correct result")
            public void testThatCorrectGetDeleteRequestToRemoveTheChatWhichIsRegisteredAndReturnedSuccessRemoving() {

                String responseBody = "Чат успешно удалён";
                wireMockServer.stubFor(delete(urlEqualTo("/tg-chat/1"))
                    .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(responseBody)));

                Optional<String> actualResponse = scrapperWebClient.deleteChat(1L);

                assertThat(actualResponse).isPresent();
                assertThat(actualResponse.get()).isEqualTo(responseBody);
            }

            @Test
            @DisplayName("/tg-chat/: DELETE; Incorrect result - user is not registered")
            public void testThatCorrectGetDeleteRequestToRemoveTheChatWhichIsNotRegisteredAndReturnedFailureExceptionChatNotFound() {
                wireMockServer.stubFor(delete(urlEqualTo("/tg-chat/1"))
                    .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(NOT_FOUND_BODY)));

                Throwable actualException = catchThrowableOfType(
                    () -> scrapperWebClient.deleteChat(1L),
                    ApiErrorException.class
                );

                assertThat(actualException)
                    .isInstanceOf(ApiErrorException.class);
            }
        }
    }

    @Nested
    class LinksTests {
        @Nested
        class PostRequests {
            @Test
            @DisplayName("/links: POST; Correct result")
            public void testThatGetCorrectPostRequestToRegisterNewLinkForUserAndReturnedSuccessRegistration()
                throws URISyntaxException {
                wireMockServer.stubFor(post(urlEqualTo("/links"))
                    .withHeader("Tg-Chat-Id", equalTo("1"))
                    .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(LINK_BODY)));

                Optional<LinkResponse> actualResponse = scrapperWebClient.addLink(
                    1L, new AddLinkRequest(new URI("123"))
                );

                assertThat(actualResponse).isPresent();
                assertThat(actualResponse.get().id()).isEqualTo(1);
                assertThat(actualResponse.get().url().getPath()).isEqualTo("123");
            }

            @Test
            @DisplayName("/links: POST; Incorrect result - invalid id")
            public void testTHatGetIncorrectPostRequestToRegisterNewLinkForUserAndReturnedExceptionOfInvalidId() {
                wireMockServer.stubFor(post(urlEqualTo("/links"))
                    .withHeader("Tg-Chat-Id", equalTo("-1"))
                    .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(INVALID_BODY)));

                Throwable actualException = catchThrowableOfType(
                    () -> scrapperWebClient.addLink(-1L, new AddLinkRequest(new URI("123"))),
                    ApiErrorException.class
                );

                assertThat(actualException)
                    .isInstanceOf(ApiErrorException.class);
            }

            @Test
            @DisplayName("/links: POST; Incorrect result - link is empty")
            public void testThatGetIncorrectPostRequestToRegisterNewLinkForUserAndReturnedExceptionOfEmptyLink() {
                wireMockServer.stubFor(post(urlEqualTo("/links"))
                    .withHeader("Tg-Chat-Id", equalTo("1"))
                    .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(INVALID_BODY)));

                Throwable actualException = catchThrowableOfType(
                    () -> scrapperWebClient.addLink(1L, new AddLinkRequest(new URI(""))),
                    ApiErrorException.class
                );

                assertThat(actualException)
                    .isInstanceOf(ApiErrorException.class);
            }

            @Test
            @DisplayName("/links: POST; Incorrect result - body is not correct")
            public void testThatGetIncorrectPostRequestToRegisterNewLinkForUserAndReturnedExceptionOfWrongBody() {
                wireMockServer.stubFor(post(urlEqualTo("/links"))
                    .withHeader("Tg-Chat-Id", equalTo("1"))
                    .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(NOT_FOUND_BODY)));

                Throwable actualException = catchThrowableOfType(
                    () -> scrapperWebClient.addLink(1L, new AddLinkRequest(new URI("123"))),
                    ApiErrorException.class
                );

                assertThat(actualException)
                    .isInstanceOf(ApiErrorException.class);
            }
        }

        @Nested
        class GetRequests {
            @Test
            @DisplayName("/links: GET; Correct result")
            public void testThatGetCorrectGetRequestToClaimLinksForUserFromTheDatabaseAndReturnedExpectedListOfLinks() {
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

                Optional<ListLinksResponse> actualResponse = scrapperWebClient.getLinks(1L);

                assertThat(actualResponse).isPresent();
                assertThat(actualResponse.get().size()).isEqualTo(1);
                assertThat(actualResponse.get().links())
                    .hasSize(1)
                    .extracting(LinkResponse::id, link -> link.url().getPath())
                    .containsExactly(tuple(1L, "link"));
            }

            @Test
            @DisplayName("/links: GET; Incorrect result - invalid id")
            public void testThatGetIncorrectGetRequestToClaimLinksForUserFromDatabaseAndReturnedExceptionOfInvalidId() {
                wireMockServer.stubFor(get(urlEqualTo("/links"))
                    .withHeader("Tg-Chat-Id", equalTo("-1"))
                    .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(INVALID_BODY)));

                Throwable actualException = catchThrowableOfType(
                    () -> scrapperWebClient.getLinks(-1L),
                    ApiErrorException.class
                );

                assertThat(actualException)
                    .isInstanceOf(ApiErrorException.class);
            }

            @Test
            @DisplayName("/links: GET; Incorrect result - wrong request")
            public void testThatGetCorrectGetRequestToClaimLinksForUserFromTheEmptyDatabaseAndReturnedExceptionOfInvalidBody() {
                wireMockServer.stubFor(get(urlEqualTo("/links"))
                    .withHeader("Tg-Chat-Id", equalTo("1"))
                    .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(NOT_FOUND_BODY)));

                Throwable actualException = catchThrowableOfType(
                    () -> scrapperWebClient.getLinks(1L),
                    ApiErrorException.class
                );

                assertThat(actualException)
                    .isInstanceOf(ApiErrorException.class);
            }
        }

        @Nested
        class DeleteRequests {
            @Test
            @DisplayName("/links: DELETE; Correct result")
            public void testThatGetCorrectDeleteRequestToRemoveTheLinkAndReturnedSuccessDeleting()
                throws URISyntaxException {
                wireMockServer.stubFor(delete(urlEqualTo("/links"))
                    .withHeader("Tg-Chat-Id", equalTo("1"))
                    .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(LINK_BODY)));

                Optional<LinkResponse> actualResponse = scrapperWebClient.removeLink(
                    1L, new RemoveLinkRequest(new URI("123"))
                );

                assertThat(actualResponse).isPresent();
                assertThat(actualResponse.get().id()).isEqualTo(1);
                assertThat(actualResponse.get().url().getPath()).isEqualTo("123");
            }

            @Test
            @DisplayName("/links: DELETE; Incorrect result - invalid id")
            public void testThatGetIncorrectDeleteRequestToRemoveTheLinkAndReturnedExceptionOfInvalidId() {
                wireMockServer.stubFor(delete(urlEqualTo("/links"))
                    .withHeader("Tg-Chat-Id", equalTo("-1"))
                    .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(INVALID_BODY)));

                Throwable actualException = catchThrowableOfType(
                    () -> scrapperWebClient.removeLink(-1L, new RemoveLinkRequest(new URI("123"))),
                    ApiErrorException.class
                );

                assertThat(actualException)
                    .isInstanceOf(ApiErrorException.class);
            }

            @Test
            @DisplayName("/links: DELETE; Incorrect result - empty link")
            public void testThatGetIncorrectDeleteRequestToRemoveTheLinkAndReturnedExceptionOfEmptyLink() {
                wireMockServer.stubFor(delete(urlEqualTo("/links"))
                    .withHeader("Tg-Chat-Id", equalTo("1"))
                    .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(INVALID_BODY)));

                Throwable actualException = catchThrowableOfType(
                    () -> scrapperWebClient.removeLink(1L, new RemoveLinkRequest(new URI(""))),
                    ApiErrorException.class
                );

                assertThat(actualException)
                    .isInstanceOf(ApiErrorException.class);
            }

            @Test
            @DisplayName("/links: DELETE; Incorrect result - wrong body")
            public void testThatGetIncorrectDeleteRequestToRemoveTheLinkAndReturnedExceptionOfWrongBody() {
                wireMockServer.stubFor(delete(urlEqualTo("/links"))
                    .withHeader("Tg-Chat-Id", equalTo("1"))
                    .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(NOT_FOUND_BODY)));

                Throwable actualException = catchThrowableOfType(
                    () -> scrapperWebClient.removeLink(1L, new RemoveLinkRequest(new URI("123"))),
                    ApiErrorException.class
                );

                assertThat(actualException)
                    .isInstanceOf(ApiErrorException.class);
            }
        }
    }
}
