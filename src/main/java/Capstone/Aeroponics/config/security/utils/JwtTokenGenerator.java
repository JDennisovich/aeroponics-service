package Capstone.Aeroponics.config.security.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import Capstone.Aeroponics.models.request.jwt.JwtRecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenGenerator {

    private final JwtEncoder jwtEncoder;

    private final JwtRecord jwtRecord;

    private final static String SCOPE = "scope";

    private final static String SCOPE_REFRESH_TOKEN = "REFRESH_TOKEN";

    private final static String SPACE_DELIMITER = " ";

    private static final long DEFAULT_EXPIRY_MINUTES = 30L;
    private static final long DEFAULT_REFRESH_HOURS = 24L;

    @PostConstruct
    private void validateConfig() {
        long expiry = jwtRecord.expiryAt();
        long refresh = jwtRecord.refreshExpiryAt();

        if (expiry <= 0) {
            log.warn("jwt.core.expiryAt is not set or invalid ({}). Falling back to default {} minutes.", expiry, DEFAULT_EXPIRY_MINUTES);
        }
        if (refresh <= 0) {
            log.warn("jwt.core.refreshExpiryAt is not set or invalid ({}). Falling back to default {} hours.", refresh, DEFAULT_REFRESH_HOURS);
        }
        log.info("JWT configuration - issuer: {}, expiryMinutes: {}, refreshHours: {}",
                jwtRecord.issuer(), expiry <= 0 ? DEFAULT_EXPIRY_MINUTES : expiry, refresh <= 0 ? DEFAULT_REFRESH_HOURS : refresh);
    }

    /**
     * Generate access token string.
     *
     * @param authentication the authentication
     * @return the string
     */
    public String generateAccessToken(Authentication authentication) {

        log.info("[JwtTokenGenerator.generateAccessToken]::invoked");
        log.debug("generating access token for: {}", authentication.getName());

        Instant now = Instant.now();
        long expiryMinutes = jwtRecord.expiryAt() > 0 ? jwtRecord.expiryAt() : DEFAULT_EXPIRY_MINUTES;
        Instant expiresAt = now.plus(expiryMinutes, ChronoUnit.MINUTES);
        if (!expiresAt.isAfter(now)) {
            // ensure we always have expiresAt > issuedAt
            expiresAt = now.plus(1, ChronoUnit.MINUTES);
            log.warn("Computed expiresAt was not after issuedAt; using fallback expiry of 1 minute from now.");
        }

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtRecord.issuer())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(authentication.getName())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }

    /**
     * Generate refresh token string.
     *
     * @param authentication the authentication
     * @return the string
     */
    public String generateRefreshToken(Authentication authentication) {

        log.info("[JwtTokenGenerator.generateRefreshToken]::invoked");
        log.debug("generating access token based on refreshToken for: {}", authentication.getName());

        Instant now = Instant.now();
        long refreshHours = jwtRecord.refreshExpiryAt() > 0 ? jwtRecord.refreshExpiryAt() : DEFAULT_REFRESH_HOURS;
        Instant expiresAt = now.plus(refreshHours, ChronoUnit.HOURS);
        if (!expiresAt.isAfter(now)) {
            expiresAt = now.plus(1, ChronoUnit.HOURS);
            log.warn("Computed refresh expiresAt was not after issuedAt; using fallback of 1 hour from now.");
        }

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtRecord.issuer())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(authentication.getName())
                .claim(SCOPE, SCOPE_REFRESH_TOKEN)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }

    private String getRolesOfUser(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(SPACE_DELIMITER));
    }
}