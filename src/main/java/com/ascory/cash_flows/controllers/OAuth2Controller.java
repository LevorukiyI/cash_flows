package com.ascory.cash_flows.controllers;

import com.ascory.cash_flows.requests.OAuth2Request;
import com.ascory.cash_flows.responses.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController("auth")
interface OAuth2Controller {
    @GetMapping("/authenticate")
    ResponseEntity<AuthenticationResponse> authenticate(@RequestParam("code") String code);

    @GetMapping("/register")
    ResponseEntity<AuthenticationResponse> register(@RequestParam("code") String code);

    //this method adds new OAuth2 verification to the account
    @PostMapping("/add-verification")
    ResponseEntity<?> addVerification(@RequestBody OAuth2Request oAuth2Request,
                                      Authentication authentication);

    @PostMapping("/delete-verification")
    ResponseEntity<?> deleteVerification(
            Authentication authentication);
}
