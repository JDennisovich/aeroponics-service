    package Capstone.Aeroponics.services;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import Capstone.Aeroponics.config.security.utils.JwtTokenGenerator;
import Capstone.Aeroponics.models.entities.RefreshToken;
import Capstone.Aeroponics.models.entities.User;
import Capstone.Aeroponics.models.enums.TokenType;
import Capstone.Aeroponics.models.request.UserRO;
import Capstone.Aeroponics.models.request.jwt.JwtRecord;
import Capstone.Aeroponics.models.response.OAuthResponse;
import Capstone.Aeroponics.repositories.RefreshTokenRepository;
import Capstone.Aeroponics.repositories.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtTokenGenerator jwtTokenGenerator;

    private final JwtRecord jwtRecord;

    private final static int EXPIRY_MULTIPLIER = 60;

    private final static String COOKIE_REFRESH_KEY = "refresh_token";

    private final static String SPLIT_EXPR = ",";

    /**
     * Gets jwt tokens after authentication.
     *
     * @param authentication the authentication
     * @param response       the response
     * @return the jwt tokens after authentication
     */
    public OAuthResponse getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {

        log.info("[OAuthService.getJwtTokensAfterAuthentication]::invoked");
        log.info("authentication {}", authentication);

        try {

            var user = userRepository
                    .findByEmail(authentication.getName())
                    .orElseThrow(() -> {
                        log.error("user :{} not found", authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
                    });

            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            this.saveUserRefreshToken(user, refreshToken);
            this.createRefreshTokenCookie(response, refreshToken);

            log.info("access token for user: {}, has been generated", user.getEmail());

            return OAuthResponse.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(Long.valueOf(jwtRecord.expiryAt()).intValue() * EXPIRY_MULTIPLIER)
                    .username(user.getEmail())
                    .tokenType(TokenType.Bearer)
                    .build();

        } catch (Exception ex) {
            log.error("exception while authenticating the user due to: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get token, please try again!");
        }
    }

    /**
     * Gets access token using refresh token.
     *
     * @param authorizationHeader the authorization header
     * @return the access token using refresh token
     */
    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {

        log.info("[OAuthService.getAccessTokenUsingRefreshToken]::invoked");
        log.debug("authorizationHeader {}", authorizationHeader);

        if (!authorizationHeader.startsWith(TokenType.Bearer.name())) {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);

        var refreshTokenEntity = refreshTokenRepository
                .findByRefreshToken(refreshToken)
                .filter(tokens -> !tokens.isRevoked())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh token revoked"));

        User user = refreshTokenEntity.getUser();
        Authentication authentication = OAuthService.createAuthenticationObject(user);
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return OAuthResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(Long.valueOf(jwtRecord.expiryAt()).intValue() * EXPIRY_MULTIPLIER)
                .username(user.getEmail())
                .tokenType(TokenType.Bearer)
                .build();
    }

    /**
     * Register user o auth response.
     *
     * @param userRO        the user info data
     * @return the o auth response
     * @throws Exception the exception
     */
    public OAuthResponse registerUser(UserRO userRO) throws Exception {

        log.info("[OAuthService.registerUser]::invoked");
        log.debug("Registration with {}", userRO);

        try {

            if (userRepository
                    .findByEmail(userRO.email())
                    .isPresent()) {
                throw new Exception("User Already Exists");
            }

            User user = userRO.toEntity(null);

            Authentication authentication = createAuthenticationObject(user);
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            User savedUser = userRepository.save(user);

            log.info("User:{} Successfully registered", savedUser.getEmail());

            return OAuthResponse.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(Long.valueOf(jwtRecord.expiryAt()).intValue() * EXPIRY_MULTIPLIER)
                    .username(savedUser.getEmail())
                    .tokenType(TokenType.Bearer)
                    .build();

        } catch (Exception ex) {
            log.error("Exception during registration: {}", ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    private void saveUserRefreshToken(User user, String refreshToken) {

        var refreshTokenEntity = RefreshToken.builder()
                .user(user)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
    }

    private void createRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie(COOKIE_REFRESH_KEY, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * EXPIRY_MULTIPLIER * EXPIRY_MULTIPLIER);
        response.addCookie(refreshTokenCookie);
    }

    private static Authentication createAuthenticationObject(User user) {

        String username = user.getEmail();
        String password = user.getPassword();
        String roles = user.getRole().toString();

        String[] roleArray = roles.split(SPLIT_EXPR);
        GrantedAuthority[] authorities = Arrays
                .stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }
}
