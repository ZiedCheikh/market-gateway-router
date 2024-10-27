package tu.startup.market.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Autowired
    private JwtDecoder jwtDecoder;

    public Jwt decodeJwt(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (JwtException e) {
            // Handle exception for invalid token
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
}