package com.ascory.cash_flows.controllers;

import com.ascory.cash_flows.requests.OAuth2Request;
import com.ascory.cash_flows.responses.AuthenticationResponse;
import com.ascory.cash_flows.services.GitHubOAuth2Service;
import com.ascory.cash_flows.services.AuthenticationRedirectResponsesAdapter;
import com.ascory.cash_flows.services.OAuth2ServiceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/github")
@RequiredArgsConstructor
public class GitHubOAuth2Controller implements OAuth2Controller{
    private final OAuth2ServiceContext oAuth2ServiceContext;
    private final GitHubOAuth2Service gitHubOAuth2Service;
    private final AuthenticationRedirectResponsesAdapter authenticationRedirectResponsesAdapter;

    @GetMapping("/authenticate")
    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestParam("code") String code) {
        // Реализация метода authenticate
        oAuth2ServiceContext.setOAuth2ServiceStrategy(gitHubOAuth2Service);
        return authenticationRedirectResponsesAdapter
                .getMainPageRedirectResponseEntity(
                        oAuth2ServiceContext.authenticate(code)
                );
    }

    @GetMapping("/register")
    @Override
    public ResponseEntity<AuthenticationResponse> register(@RequestParam("code") String code) {
        oAuth2ServiceContext.setOAuth2ServiceStrategy(gitHubOAuth2Service);
        return authenticationRedirectResponsesAdapter
                .getMainPageRedirectResponseEntity(
                        oAuth2ServiceContext.register(code)
                );
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