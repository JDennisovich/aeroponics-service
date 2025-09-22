package Capstone.Aeroponics.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import Capstone.Aeroponics.config.security.utils.JwtTokenUtils;
import Capstone.Aeroponics.models.request.jwt.RSAKeyRecord;
import Capstone.Aeroponics.repositories.RefreshTokenRepository;
import Capstone.Aeroponics.services.LogoutHandlerService;
import Capstone.Aeroponics.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final RSAKeyRecord rsaKeyRecord;

    private final UserService userService;

    private final UserInfoServiceWithFilter userInfoServiceWithFilter;

    private final LogoutHandlerService logoutHandlerService;

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtTokenUtils jwtTokenUtils;

    @Order(0)
    @Bean
    public SecurityFilterChain publicSaveUserSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/user") //TODO: add "/api/tower" if need for testing
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/user").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/user").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/plant").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/tower").permitAll() TODO: Remove comment
//                        .requestMatchers(HttpMethod.GET, "/api/tower").permitAll()
//                        .requestMatchers(HttpMethod.DELETE, "/api/tower/*").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/tower/user/*").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(this::handleAuthException))
                .build();
    }

    @Order(1)
    @Bean
    public SecurityFilterChain signInSecurityFilterChain(HttpSecurity http)
    throws Exception {
    return commonSecurityConfig(http, "/oauth/login/**")
    .userDetailsService(userService)
    .httpBasic(withDefaults())
    .build();
    }

    @Order(2)
    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws
    Exception {
    return commonJwtSecurityConfig(http, "/api/**")
    .addFilterBefore(new JwtAccessTokenFilter(rsaKeyRecord, jwtTokenUtils),
    UsernamePasswordAuthenticationFilter.class)
    .httpBasic(withDefaults())
    .build();
    }

    @Order(3)
    @Bean
    public SecurityFilterChain refreshTokenSecurityFilterChain(HttpSecurity http)
    throws Exception {
    return commonJwtSecurityConfig(http, "/oauth/refresh-token/**")
    .addFilterBefore(new JwtRefreshTokenFilter(rsaKeyRecord, jwtTokenUtils,
    refreshTokenRepository),
    UsernamePasswordAuthenticationFilter.class)
    .httpBasic(withDefaults())
    .build();
    }

    @Order(4)
    @Bean
    public SecurityFilterChain logoutSecurityFilterChain(HttpSecurity http)
    throws Exception {
    return commonJwtSecurityConfig(http, "/oauth/logout/**")
    .logout(logout -> logout
    .logoutUrl("/oauth/logout")
    .addLogoutHandler(logoutHandlerService)
    .logoutSuccessHandler((_, _, _) -> SecurityContextHolder.clearContext()))
    .addFilterBefore(new JwtAccessTokenFilter(rsaKeyRecord, jwtTokenUtils),
    UsernamePasswordAuthenticationFilter.class)
    .build();
    }

    @Order(5)
    @Bean
    public SecurityFilterChain registerSecurityFilterChain(HttpSecurity http)
    throws Exception {
    return commonSecurityConfig(http, "/oauth/register/**")
    .userDetailsService(userInfoServiceWithFilter)
    .httpBasic(withDefaults())
    .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeyRecord.publicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeyRecord.publicKey())
                .privateKey(rsaKeyRecord.privateKey())
                .build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource =
                new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }

    private HttpSecurity commonSecurityConfig(HttpSecurity http, String... pattern) throws Exception {
        return http
                .securityMatcher(pattern)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(this::handleAuthException));
    }

    private HttpSecurity commonJwtSecurityConfig(HttpSecurity http, String... pattern) throws Exception {
        return commonSecurityConfig(http, pattern)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                    ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                });
    }

    private void handleAuthException(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        log.error("Authentication failed: {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}