package com.internsprint.controller;

import com.internsprint.dto.ApiResponse;
import com.internsprint.dto.LoginRequest;
import com.internsprint.dto.RegisterRequest;
import com.internsprint.model.User;
import com.internsprint.repository.CompanyRepository;
import com.internsprint.repository.UserRepository;
import com.internsprint.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

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

    // One-time production setup endpoint
    // Creates admin user and verifies all companies
    @GetMapping("/init-production")
    public ResponseEntity<String> initProduction() {
        try {
            // Insert admin if not exists
            jdbcTemplate.execute(
                "INSERT IGNORE INTO users (name, email, password_hash, role, is_active, created_at, updated_at) " +
                "VALUES ('Admin', 'admin@internsprint.com', " +
                "'$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', " +
                "'admin', 1, NOW(), NOW())"
            );
            // Verify all companies
            jdbcTemplate.execute("UPDATE companies SET is_verified = 1");

            return ResponseEntity.ok(
                "Production initialized! Admin: admin@internsprint.com / password. All companies verified."
            );
        } catch (Exception e) {
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }
}