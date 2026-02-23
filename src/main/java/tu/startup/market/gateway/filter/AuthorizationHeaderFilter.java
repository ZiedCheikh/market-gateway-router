package tu.startup.market.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tu.startup.market.gateway.service.JwtService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationHeaderFilter.class);
    @Autowired
    JwtService jwtService;

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(final Config config) {
        return (exchange, chain) -> {

            final ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsHeader("Authorization")) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            final String authorizationHeader = request.getHeaders().get("Authorization").get(0);
            final String jwt = authorizationHeader.replace("Bearer", "").trim();

            final List<String> authorities = getAuthorities(jwt);

            final boolean hasRequiredAuthority = authorities.stream()
                    .anyMatch(authority -> config.getAuthorities().contains(authority));

            if (!hasRequiredAuthority) {
                return onError(exchange, "User is not authorized to perform this operation", HttpStatus.FORBIDDEN);
            }
            return chain.filter(exchange);
        };
    }

    private List<String> getAuthorities(final String jwt) {
        final List<String> returnValue = new ArrayList<>();
        try {
            final Jwt parsedToken = jwtService.decodeJwt(jwt);
            @SuppressWarnings("unchecked")
            final List<String> scopes = (List<String>) parsedToken.getClaims().get("scope");
            for (final String scope : scopes) {
                returnValue.add(scope);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return returnValue;
    }

    private Mono<Void> onError(final ServerWebExchange exchange, final String error, final HttpStatus httpStatus) {
        final ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
        final byte[] bytes = error.getBytes(StandardCharsets.UTF_8);
        final DataBuffer buffer = response.bufferFactory().wrap(bytes);
        response.writeWith(Mono.just(buffer));
        return response.setComplete();
    }


    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("authorities");
    }

    public static class Config {
        private List<String> authorities;

        public List<String> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(final String authorities) {
            this.authorities = Arrays.asList(authorities.split(" "));
        }
    }
}
