package Capstone.Aeroponics.models.request.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt.core")
public record JwtRecord(long expiryAt, long refreshExpiryAt, String issuer) {

}
