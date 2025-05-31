package tu.startup.market.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();

        logger.info(">>> Incoming request: {} {}", request.getMethod(), request.getURI());
        logger.info("Headers: {}", request.getHeaders());

        return chain.filter(exchange)
                .doOnSuccess(aVoid -> {
                    final ServerHttpResponse response = exchange.getResponse();
                    logger.info("<<< Response status: {}", response.getStatusCode());
                })
                .doOnError(error -> {
                    logger.error("!!! Error during request processing", error);
                });
    }
}
