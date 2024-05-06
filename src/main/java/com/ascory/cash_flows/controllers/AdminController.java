package com.ascory.cash_flows.controllers;

import com.ascory.cash_flows.requests.DeleteUserRequest;
import com.ascory.cash_flows.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllUsers(){
        return adminService.getAllUsers();
    }

    @PostMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteUserRequest deleteUserRequest){
        adminService.deleteUser(deleteUserRequest.getId());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
