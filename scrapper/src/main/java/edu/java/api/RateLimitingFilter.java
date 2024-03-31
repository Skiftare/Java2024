package edu.java.api;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class RateLimitingFilter implements WebFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final static int CAPACITY_OF_REQUESTS = 1000;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();

        Bucket requestBucket = buckets.computeIfAbsent(ip, k -> createNewBucket());

        if (requestBucket.tryConsume(1)) {
            return chain.filter(exchange);
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }
    }

    private Bucket createNewBucket() {
        return Bucket4j.builder()
            .addLimit(Bandwidth.classic(
                CAPACITY_OF_REQUESTS,
                Refill.intervally(CAPACITY_OF_REQUESTS,
                    Duration.ofMinutes(1)
                )
            ))
            .build();
    }
}
