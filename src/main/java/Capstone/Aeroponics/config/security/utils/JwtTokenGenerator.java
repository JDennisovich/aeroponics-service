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

import Capstone.Aeroponics.models.enums.RoleType;
import Capstone.Aeroponics.models.request.jwt.JwtRecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenGenerator {

    private final JwtEncoder jwtEncoder;

    private final JwtRecord jwtRecord;

    private final static String SCOPE = "scope";

    private final static String SCOPE_REFRESH_TOKEN = "REFRESH_TOKEN";

    private final static String SPACE_DELIMITER = " ";

    /**
     * Generate access token string.
     *
     * @param authentication the authentication
     * @return the string
     */
    public String generateAccessToken(Authentication authentication) {

        log.info("[JwtTokenGenerator.generateAccessToken]::invoked");
        log.debug("generating access token for: {}", authentication.getName());

        String roles = this.getRolesOfUser(authentication);
        String permissions = this.getPermissionsFromRoles(roles);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtRecord.issuer())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(jwtRecord.expiryAt(), ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim(SCOPE, permissions)
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

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtRecord.issuer())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(jwtRecord.refreshExpiryAt(), ChronoUnit.HOURS))
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

    private String getPermissionsFromRoles(String roles) {

        Set<String> permissions = new HashSet<>();

        if (roles.contains(RoleType.ADMIN.name())) {
            permissions.addAll(RoleType.ADMIN.getStringPermissions());
        }

        if (roles.contains(RoleType.BASIC.name())) {
            permissions.addAll(RoleType.BASIC.getStringPermissions());
        }

        return String.join(SPACE_DELIMITER, permissions);
    }
}