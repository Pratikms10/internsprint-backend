package com.internsprint.controller;

import com.internsprint.dto.ApiResponse;
import com.internsprint.dto.LoginRequest;
import com.internsprint.dto.RegisterRequest;
import com.internsprint.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok("Registration successful",
                        authService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok("Login successful",
                        authService.login(request)));
    }
}
