package tu.startup.market.gateway.web.handler;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import tu.startup.market.gateway.web.model.SsoUser;

@Component
public class UserHandler {

    public Mono<ServerResponse> getUserInfo(final ServerRequest serverRequest) {
        return serverRequest.principal()
                .cast(OAuth2AuthenticationToken.class)
                .flatMap(token -> {
                    final OAuth2User user = token.getPrincipal();
                    final SsoUser ssoUser = new SsoUser(user.getName());
                    return ServerResponse.ok().bodyValue(ssoUser);
                })
                .switchIfEmpty(ServerResponse.status(401).bodyValue("Anonymous or not authenticated"));
    }
}

