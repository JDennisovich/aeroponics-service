package Capstone.Aeroponics.converter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class JwtConverter {

    public static Jwt convertToSpringJwt(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);

        Map<String, Object> headers = new HashMap<>(decodedJWT.getHeaderClaim("header").asMap());
        Map<String, Object> claims = new HashMap<>(decodedJWT.getClaims());

        String issuer = decodedJWT.getIssuer();
        String subject = decodedJWT.getSubject();
        Instant issuedAt = decodedJWT.getIssuedAt().toInstant();
        Instant expiresAt = decodedJWT.getExpiresAt().toInstant();

        return Jwt.withTokenValue(token)
            .headers(h -> h.putAll(headers))
            .claims(c -> c.putAll(claims))
            .issuer(issuer)
            .subject(subject)
            .issuedAt(issuedAt)
            .expiresAt(expiresAt)
            .build();
    }
}