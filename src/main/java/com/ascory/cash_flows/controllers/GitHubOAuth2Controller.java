package com.ascory.cash_flows.controllers;

import com.ascory.cash_flows.requests.OAuth2Request;
import com.ascory.cash_flows.responses.AuthenticationResponse;
import com.ascory.cash_flows.services.GitHubOAuth2Service;
import com.ascory.cash_flows.services.OAuth2ServiceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/github")
@RequiredArgsConstructor
public class GitHubOAuth2Controller implements OAuth2Controller{
    private final OAuth2ServiceContext oAuth2ServiceContext;
    private final GitHubOAuth2Service gitHubOAuth2Service;

    @PostMapping("/authenticate")
    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody OAuth2Request oAuth2Request) {
        // Реализация метода authenticate
        oAuth2ServiceContext.setOAuth2ServiceStrategy(gitHubOAuth2Service);
        return ResponseEntity.ok(oAuth2ServiceContext.authenticate(oAuth2Request.getCode()));
    }

    @PostMapping("/register")
    @Override
    public ResponseEntity<AuthenticationResponse> register(@RequestBody OAuth2Request oAuth2Request) {
        oAuth2ServiceContext.setOAuth2ServiceStrategy(gitHubOAuth2Service);
        return ResponseEntity.ok(oAuth2ServiceContext.register(oAuth2Request.getCode()));
    }

    @PostMapping("/add-verification")
    @Override
    public ResponseEntity<?> addVerification(
            @RequestBody OAuth2Request oAuth2Request,
            Authentication authentication) {
        oAuth2ServiceContext.setOAuth2ServiceStrategy(gitHubOAuth2Service);
        oAuth2ServiceContext.addVerification(oAuth2Request.getCode(), authentication);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/delete-verification")
    @Override
    public ResponseEntity<?> deleteVerification(
            Authentication authentication){
        oAuth2ServiceContext.setOAuth2ServiceStrategy(gitHubOAuth2Service);
        oAuth2ServiceContext.deleteVerification(authentication);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}