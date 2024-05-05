package com.ascory.cash_flows.controllers;

import com.ascory.cash_flows.requests.OAuth2Request;
import com.ascory.cash_flows.responses.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("auth")
interface OAuth2Controller {
    @PostMapping("/authenticate")
    ResponseEntity<AuthenticationResponse> authenticate(@RequestBody OAuth2Request oAuth2Request);

    @PostMapping("/register")
    ResponseEntity<AuthenticationResponse> register(@RequestBody OAuth2Request oAuth2Request);

    //this method adds new OAuth2 verification to the account
    @PostMapping("/add-verification")
    ResponseEntity<?> addVerification(@RequestBody OAuth2Request oAuth2Request,
                                      Authentication principal);
    @PostMapping("/delete-verification")
    ResponseEntity<?> deleteVerification(
            Authentication authentication);
}
