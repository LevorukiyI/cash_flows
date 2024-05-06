package com.ascory.cash_flows.controllers;

import com.ascory.cash_flows.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get-all-verifications")
    public ResponseEntity<?> getAllVerifications(Authentication authentication){
        return ResponseEntity.ok(userService.getAllVerifications(authentication));
    }

    @GetMapping("/get-role")
    public ResponseEntity<?> getRole(Authentication authentication){
        return userService.getRole(authentication);
    }
}
