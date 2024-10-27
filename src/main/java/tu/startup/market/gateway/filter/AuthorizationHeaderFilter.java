package tu.startup.market.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tu.startup.market.gateway.service.JwtService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Autowired
    JwtService jwtService;

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey("Authorization")) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get("Authorization").get(0);
            String jwt = authorizationHeader.replace("Bearer", "").trim();

            List<String> authorities = getAuthorities(jwt);

            boolean hasRequiredAuthority = authorities.stream()
                    .anyMatch(authority -> config.getAuthorities().contains(authority));

            if (!hasRequiredAuthority)
                return onError(exchange, "User is not authorized to perform this operation", HttpStatus.FORBIDDEN);
            return chain.filter(exchange);
        };
    }

    private List<String> getAuthorities(String jwt) {
        List<String> returnValue = new ArrayList<>();
        try {
            Jwt parsedToken = jwtService.decodeJwt(jwt);
            List<String> scopes = (List<String>) parsedToken.getClaims().get("scope");
            for (String scope : scopes) {
                returnValue.add(scope);
            }
        } catch (Exception ex) {
            return returnValue;
        }
        return returnValue;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
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
        public void setAuthorities(String authorities) {
            this.authorities = Arrays.asList(authorities.split(" "));
        }
    }
}
