    package Capstone.Aeroponics.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import Capstone.Aeroponics.services.EmailService;
import Capstone.Aeroponics.services.OtpService;

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
    
    private final EmailService emailService;
    
    private final OtpService otpService;

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
     * Send OTP for user registration
     *
     * @param email the user email
     * @return success message
     * @throws Exception the exception
     */
    public String sendRegistrationOtp(String email) throws Exception {
        log.info("[OAuthService.sendRegistrationOtp]::invoked for email: {}", email);

        try {
            // Check if user already exists
            if (userRepository.findByEmail(email).isPresent()) {
                throw new Exception("User already exists with this email");
            }

            // Check if user already has an active OTP
            if (otpService.hasActiveOtp(email)) {
                throw new Exception("OTP already sent. Please check your email or wait for it to expire.");
            }

            // Generate and send OTP
            String otpCode = otpService.generateOtp(email);
            emailService.sendOtpEmail(email, otpCode, "User");

            log.info("OTP sent successfully to: {}", email);
            return "OTP sent successfully to your email";

        } catch (Exception ex) {
            log.error("Exception during OTP sending: {}", ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Verify OTP and register user
     *
     * @param userRO the user info data
     * @param otpCode the OTP code
     * @return the o auth response
     * @throws Exception the exception
     */
    public OAuthResponse verifyOtpAndRegisterUser(UserRO userRO, String otpCode) throws Exception {
        log.info("[OAuthService.verifyOtpAndRegisterUser]::invoked");
        log.debug("Registration with OTP verification for email: {}", userRO.email());

        try {
            // Verify OTP
            if (!otpService.verifyOtp(userRO.email(), otpCode)) {
                throw new Exception("Invalid or expired OTP");
            }

            // Check if user already exists
            if (userRepository.findByEmail(userRO.email()).isPresent()) {
                throw new Exception("User Already Exists");
            }

            User user = userRO.toEntity(null);
            Authentication authentication = createAuthenticationObject(user);
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            User savedUser = userRepository.save(user);

            log.info("User:{} Successfully registered with OTP verification", savedUser.getEmail());

            return OAuthResponse.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(Long.valueOf(jwtRecord.expiryAt()).intValue() * EXPIRY_MULTIPLIER)
                    .username(savedUser.getEmail())
                    .tokenType(TokenType.Bearer)
                    .build();

        } catch (Exception ex) {
            log.error("Exception during OTP verification and registration: {}", ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Send OTP for change password
     *
     * @param email the user email
     * @return success message
     * @throws Exception the exception
     */
    public String sendChangePasswordOtp(String email) throws Exception {
        log.info("[OAuthService.sendChangePasswordOtp]::invoked for email: {}", email);

        try {
            // Check if user exists
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("User not found with this email"));

            // Check if user already has an active OTP
            if (otpService.hasActiveOtp(email)) {
                throw new Exception("OTP already sent. Please check your email or wait for it to expire.");
            }

            // Generate and send OTP
            String otpCode = otpService.generateOtp(email);
            emailService.sendChangePasswordOtpEmail(email, otpCode, user.getFirst_name());

            log.info("Change password OTP sent successfully to: {}", email);
            return "OTP sent successfully to your email for password change";

        } catch (Exception ex) {
            log.error("Exception during change password OTP sending: {}", ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Verify OTP and change password
     *
     * @param email the user email
     * @param newPassword the new password
     * @param confirmPassword the confirm password
     * @param otpCode the OTP code
     * @return success message
     * @throws Exception the exception
     */
    public String verifyOtpAndChangePassword(String email, String newPassword, String confirmPassword, String otpCode) throws Exception {
        log.info("[OAuthService.verifyOtpAndChangePassword]::invoked for email: {}", email);

        try {
            // Verify OTP
            if (!otpService.verifyOtp(email, otpCode)) {
                throw new Exception("Invalid or expired OTP");
            }

            // Check if user exists
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("User not found"));

            // Validate passwords match
            if (!newPassword.equals(confirmPassword)) {
                throw new Exception("Passwords do not match");
            }

            // Update password
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            log.info("Password changed successfully for user: {}", email);
            return "Password changed successfully";

        } catch (Exception ex) {
            log.error("Exception during OTP verification and password change: {}", ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Register user o auth response (legacy method - now requires OTP)
     *
     * @param userRO        the user info data
     * @return the o auth response
     * @throws Exception the exception
     */
    public OAuthResponse registerUser(UserRO userRO) throws Exception {
        throw new Exception("Registration now requires OTP verification. Please use sendRegistrationOtp and verifyOtpAndRegisterUser endpoints.");
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

        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
