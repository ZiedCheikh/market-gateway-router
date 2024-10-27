package tu.startup.market.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtConfig {

    @Value("${spring.security.oauth2.client.provider.spring.issuer-uri}")
    private String issuerUri;


    private static final String JWKS_URL = "http://localhost:9000/oauth2/jwks";

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(JWKS_URL).build();
    }
}