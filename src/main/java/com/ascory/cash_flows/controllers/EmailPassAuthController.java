package com.ascory.cash_flows.controllers;


import com.ascory.cash_flows.requests.EmailPassAuthenticateRequest;
import com.ascory.cash_flows.requests.EmailPassRegisterRequestEntity;
import com.ascory.cash_flows.requests.VerifyEmailPassTokenRequest;
import com.ascory.cash_flows.responses.AuthenticationResponse;
import com.ascory.cash_flows.services.EmailPassAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/email-pass")
@RequiredArgsConstructor
class EmailPassAuthController{

    private final EmailPassAuthService emailPassAuthService;

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

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(
            @RequestBody VerifyEmailPassTokenRequest verifyEmailPassTokenRequest) {
        return ResponseEntity.ok(emailPassAuthService.verifyEmailPassToken(verifyEmailPassTokenRequest.getToken()));
    }

    @PostMapping("/delete-verification")
    public ResponseEntity<?> deleteVerification(
            Authentication authentication){
        emailPassAuthService.deleteVerification(authentication);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}