package com.internsprint.controller;

import com.internsprint.dto.ApiResponse;
import com.internsprint.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getAllUsers() {
        return ResponseEntity.ok(
                ApiResponse.ok("Users fetched", adminService.getAllUsers()));
    }

    @GetMapping("/companies")
    public ResponseEntity<ApiResponse> getAllCompanies() {
        return ResponseEntity.ok(
                ApiResponse.ok("Companies fetched", adminService.getAllCompanies()));
    }

    @GetMapping("/internships")
    public ResponseEntity<ApiResponse> getAllInternships() {
        return ResponseEntity.ok(
                ApiResponse.ok("Internships fetched",
                        adminService.getAllInternships()));
    }

    @PutMapping("/companies/{id}/verify")
    public ResponseEntity<ApiResponse> verifyCompany(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok("Company verified", adminService.verifyCompany(id)));
    }

    @PutMapping("/companies/{id}/unverify")
    public ResponseEntity<ApiResponse> unverifyCompany(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok("Company unverified",
                        adminService.unverifyCompany(id)));
    }

    @DeleteMapping("/internships/{id}")
    public ResponseEntity<ApiResponse> deleteInternship(@PathVariable Long id) {
        adminService.deleteInternship(id);
        return ResponseEntity.ok(ApiResponse.ok("Internship deleted"));
    }

    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<ApiResponse> deactivateUser(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok("User deactivated",
                        adminService.deactivateUser(id)));
    }
}
