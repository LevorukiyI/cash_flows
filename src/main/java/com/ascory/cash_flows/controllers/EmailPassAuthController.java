package com.ascory.cash_flows.controllers;

import com.ascory.cash_flows.requests.EmailPassAuthenticateRequest;
import com.ascory.cash_flows.requests.EmailPassRegisterRequestEntity;
import com.ascory.cash_flows.responses.AuthenticationResponse;
import com.ascory.cash_flows.services.AuthenticationRedirectResponsesAdapter;
import com.ascory.cash_flows.services.EmailPassAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/email-pass")
@RequiredArgsConstructor
class EmailPassAuthController{

    private final EmailPassAuthService emailPassAuthService;
    private final AuthenticationRedirectResponsesAdapter authenticationRedirectResponsesAdapter;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody EmailPassAuthenticateRequest emailPassAuthenticateRequest) {
        return ResponseEntity.ok(emailPassAuthService.authenticate(emailPassAuthenticateRequest));
    }

    @PostMapping("/get-register-token")
    public ResponseEntity<?> getRegisterTokenOnEmail(
            @RequestBody EmailPassRegisterRequestEntity emailPassRegisterRequestEntity) {
        emailPassAuthService.createEmailVerificationToken(emailPassRegisterRequestEntity, null);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/get-verification-token")
    public ResponseEntity<?> getAddVerificationTokenOnEmail(
            @RequestBody EmailPassRegisterRequestEntity emailPassRegisterRequestEntity,
            Authentication authentication) {
        if (authentication == null) {
            throw new AccessDeniedException("Access is denied. User is unauthenticated or did not provide a JWT token.");
        }
        emailPassAuthService.createEmailVerificationToken(emailPassRegisterRequestEntity, authentication);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(
            @RequestParam("token") String token) {
        return authenticationRedirectResponsesAdapter
                .getMainPageRedirectResponseEntity(
                        emailPassAuthService.verifyEmailPassToken(token)
                );
    }

    @PostMapping("/delete-verification")
    public ResponseEntity<?> deleteVerification(
            Authentication authentication){
        emailPassAuthService.deleteVerification(authentication);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}