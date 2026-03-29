package com.internsprint.controller;

import com.internsprint.dto.*;
import com.internsprint.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/company")
@PreAuthorize("hasRole('COMPANY')")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/internships")
    public ResponseEntity<ApiResponse> postInternship(
            Principal principal,
            @Valid @RequestBody InternshipRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Internship posted",
                companyService.postInternship(principal.getName(), request)));
    }

    @GetMapping("/internships")
    public ResponseEntity<ApiResponse> getMyInternships(Principal principal) {
        return ResponseEntity.ok(ApiResponse.ok("Internships fetched",
                companyService.getMyInternships(principal.getName())));
    }

    @PutMapping("/internships/{id}/close")
    public ResponseEntity<ApiResponse> closeInternship(
            Principal principal,
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Internship closed",
                companyService.closeInternship(principal.getName(), id)));
    }

    @GetMapping("/internships/{id}/applications")
    public ResponseEntity<ApiResponse> getApplications(
            Principal principal,
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Applications fetched",
                companyService.getApplications(principal.getName(), id)));
    }

    @PutMapping("/applications/{id}/status")
    public ResponseEntity<ApiResponse> updateStatus(
            Principal principal,
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.ok("Status updated",
                companyService.updateApplicationStatus(
                        principal.getName(), id, status)));
    }

    @PutMapping("/applications/{id}/schedule")
    public ResponseEntity<ApiResponse> scheduleInterview(
            Principal principal,
            @PathVariable Long id,
            @RequestParam String interviewDate) {
        LocalDateTime dt = LocalDateTime.parse(interviewDate);
        return ResponseEntity.ok(ApiResponse.ok("Interview scheduled",
                companyService.scheduleInterview(
                        principal.getName(), id, dt)));
    }
}
