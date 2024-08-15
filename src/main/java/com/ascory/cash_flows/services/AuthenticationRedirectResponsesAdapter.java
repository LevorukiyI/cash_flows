package com.ascory.cash_flows.services;

import com.ascory.cash_flows.responses.AuthenticationResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@Getter
@RequiredArgsConstructor
public class AuthenticationRedirectResponsesAdapter {

    @Value("${application.main-page-url}")
    private String mainPageUrl;

    @Value("${application.verifications-page-url}")
    private String verificationsPageUrl;

    public ResponseEntity<AuthenticationResponse> getMainPageRedirectResponseEntity(
            AuthenticationResponse authenticationResponse){
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(mainPageUrl))
                .headers(new CookieBuilder().addCookie(authenticationResponse).build())
                .body(authenticationResponse);
    }

    public ResponseEntity<HttpStatus> getVerificationsPageRedirectResponseEntity(){
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(verificationsPageUrl))
                .body(HttpStatus.OK);
    }
}
