package com.ascory.cash_flows.controllers;

import com.ascory.cash_flows.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get-all-verifications")
    public ResponseEntity<?> getAllVerifications(Authentication authentication){

        return ResponseEntity.ok().build();
    }
}
