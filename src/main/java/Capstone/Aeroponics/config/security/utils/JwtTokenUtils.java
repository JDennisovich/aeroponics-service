package Capstone.Aeroponics.config.security.utils;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Objects;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import Capstone.Aeroponics.config.security.UserInfoDetails;
import Capstone.Aeroponics.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    private final UserRepository userRepository;

    public String getUsername(Jwt jwtToken) {
        return jwtToken.getSubject();
    }

    public UserDetails userDetails(String email) {
        return userRepository
                .findByEmail(email)
                .map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format("user with email {0} does not exist", email)));
    }

    private boolean isTokenExpired(Jwt jwtToken) {
        return Objects
                .requireNonNull(jwtToken.getExpiresAt())
                .isBefore(Instant.now());
    }

    public boolean isTokenValid(Jwt jwtToken, UserDetails userDetails) {
        return !this.isTokenExpired(jwtToken) && this.getUsername(jwtToken)
                .equals(userDetails.getUsername());
    }
}
