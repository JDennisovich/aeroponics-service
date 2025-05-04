package Capstone.Aeroponics.controllers;

import Capstone.Aeroponics.models.request.UserRO;
import Capstone.Aeroponics.models.response.OAuthResponse;
import Capstone.Aeroponics.services.OAuthService;
import Capstone.Aeroponics.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OAuthResponse> authenticate(Authentication authentication, HttpServletResponse response) {
        return ResponseEntity.ok(oAuthService.getJwtTokensAfterAuthentication(authentication, response));
    }

    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping(value = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return ResponseEntity.ok(oAuthService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@Valid @RequestBody UserRO userRO,
                                      BindingResult bindingResult,
                                      HttpServletResponse httpServletResponse) {

        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        try {
            return ResponseEntity.ok(oAuthService.registerUser(userRO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage()));
        }
    }
}