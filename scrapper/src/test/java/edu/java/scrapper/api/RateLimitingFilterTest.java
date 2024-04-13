package edu.java.scrapper.api;

import edu.java.api.RateLimitingFilter;
import java.net.InetSocketAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RateLimitingFilterTest {

    private RateLimitingFilter rateLimitingFilter;
    private WebFilterChain filterChain;

    @BeforeEach
    void setUp() {
        rateLimitingFilter = new RateLimitingFilter();
        filterChain = mock(WebFilterChain.class);
        when(filterChain.filter(any())).thenReturn(Mono.empty());
    }

    @Test
    void shouldPassRequestWhenBucketIsNotExhausted() {
        MockServerHttpRequest request =
            MockServerHttpRequest.get("http://localhost").remoteAddress(new InetSocketAddress("127.0.0.1", 8080))
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(rateLimitingFilter.filter(exchange, filterChain))
            .expectComplete()
            .verify();

        verify(filterChain, times(1)).filter(exchange);
    }

    @Test
    void shouldRejectRequestWhenBucketIsExhausted() {
        MockServerHttpRequest request =
            MockServerHttpRequest.get("http://localhost").remoteAddress(new InetSocketAddress("127.0.0.1", 8080))
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        for (int i = 0; i < 1001; i++) {
            rateLimitingFilter.filter(exchange, filterChain).block();
        }

        StepVerifier.create(rateLimitingFilter.filter(exchange, filterChain))
            .expectComplete()
            .verify();

        assert HttpStatus.TOO_MANY_REQUESTS.equals(exchange.getResponse().getStatusCode());
    }
}
