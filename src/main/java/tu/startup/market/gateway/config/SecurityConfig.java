package tu.startup.market.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final String authSvrHostUrl;
    private final String authSuccessRedirectUrl;
    private final String logoutSuccessRedirectUrl;

    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfig(@Value("${auth.server.host.url}") final String authSvrHostUrl,
                          @Value("${auth.success.redirect.url}") final String authSuccessRedirectUrl,
                          @Value("${logout.success.redirect.url}") final String logoutSuccessRedirectUrl,
                          final ReactiveClientRegistrationRepository clientRegistrationRepository) {
        this.authSvrHostUrl = authSvrHostUrl;
        this.logoutSuccessRedirectUrl = logoutSuccessRedirectUrl;
        this.authSuccessRedirectUrl = authSuccessRedirectUrl;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    //@formatter:off
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        http.cors(Customizer.withDefaults())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/user/sso").permitAll())
                .securityMatcher(new NegatedServerWebExchangeMatcher(
                        ServerWebExchangeMatchers.pathMatchers("/portal/**", "/actuator/health")))
                .authorizeExchange((authorize) -> authorize.anyExchange().authenticated())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .oauth2Login(oauth2 -> oauth2.authenticationSuccessHandler(redirectToAngular()))
                .oauth2Login(Customizer.withDefaults())
                .logout(logoutSpec -> {
                    logoutSpec
                            .logoutUrl("/user/logout") // URL pour logout
                            .logoutHandler(oidcLogoutHandler())
                            .logoutSuccessHandler(new VoidServerLogoutSuccessHandler());
                })
                .oauth2Client(Customizer.withDefaults());
        return http.build();
    }

    private ServerAuthenticationSuccessHandler redirectToAngular() {
        return (webFilterExchange, authentication) -> {
            final ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
            response.setStatusCode(HttpStatus.FOUND);
            response.getHeaders().setLocation(URI.create(authSuccessRedirectUrl));
            return Mono.empty();
        };
    }

    private ServerLogoutHandler oidcLogoutHandler() {
        return (exchange, authentication) -> {
            final ServerWebExchange serverExchange = exchange.getExchange();

            // Invalidate the WebSession
            final Mono<Void> invalidateSession = serverExchange.getSession()
                    .flatMap(WebSession::invalidate);
            // Clear SecurityContext
            final Mono<Void> clearContext = Mono.fromRunnable(SecurityContextHolder::clearContext);
            // Build OIDC End Session URL if OIDC user exists
            final Mono<Void> redirectToOP = Mono.defer(() -> {
                if (authentication instanceof OAuth2AuthenticationToken oauthToken
                        && oauthToken.getPrincipal() instanceof OidcUser oidcUser) {

                    final String idToken = oidcUser.getIdToken().getTokenValue();
                    final String endSessionEndpoint = authSvrHostUrl+"/startup/authserver/connect/logout";

                    final String logoutUrl = UriComponentsBuilder.fromUriString(endSessionEndpoint)
                            .queryParam("id_token_hint", idToken)
                            .queryParam("post_logout_redirect_uri", logoutSuccessRedirectUrl)
                            .toUriString();

                    final ServerHttpResponse response = serverExchange.getResponse();
                    response.setStatusCode(HttpStatus.OK);
                    response.getHeaders().add("Content-Type", "application/json");

                    final String jsonBody = "{ \"logoutUrl\": \"" + logoutUrl + "\" }";

                    final byte[] bytes = jsonBody.getBytes(StandardCharsets.UTF_8);
                    final DataBuffer buffer = response.bufferFactory().wrap(bytes);

                    return response.writeWith(Mono.just(buffer));
                }
                return Mono.empty();
            });
            return invalidateSession
                    .then(clearContext)
                    .then(redirectToOP);
        };
    }
    //@formatter:on

}
