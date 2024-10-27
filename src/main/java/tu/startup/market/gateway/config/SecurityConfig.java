package tu.startup.market.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.securityMatcher(new NegatedServerWebExchangeMatcher(
                        ServerWebExchangeMatchers.pathMatchers("/actuator/health")))
                .authorizeExchange((authorize) -> authorize.anyExchange().authenticated())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .oauth2Login(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults());
        return http.build();
    }


}
