package com.internsprint.controller;

import com.internsprint.dto.*;
import com.internsprint.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasRole('STUDENT')")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getProfile(Principal principal) {
        return ResponseEntity.ok(ApiResponse.ok("Profile fetched",
                studentService.getProfile(principal.getName())));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse> updateProfile(
            Principal principal,
            @RequestBody StudentProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Profile updated",
                studentService.updateProfile(principal.getName(), request)));
    }

    @PostMapping("/apply/{internshipId}")
    public ResponseEntity<ApiResponse> apply(
            Principal principal,
            @PathVariable Long internshipId,
            @RequestBody ApplicationRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Application submitted",
                studentService.apply(principal.getName(), internshipId, request)));
    }

    @GetMapping("/applications")
    public ResponseEntity<ApiResponse> getMyApplications(Principal principal) {
        return ResponseEntity.ok(ApiResponse.ok("Applications fetched",
                studentService.getMyApplications(principal.getName())));
    }

    @GetMapping("/notifications")
    public ResponseEntity<ApiResponse> getNotifications(Principal principal) {
        return ResponseEntity.ok(ApiResponse.ok("Notifications fetched",
                studentService.getNotifications(principal.getName())));
    }

    @PutMapping("/notifications/read")
    public ResponseEntity<ApiResponse> markRead(Principal principal) {
        studentService.markNotificationsRead(principal.getName());
        return ResponseEntity.ok(ApiResponse.ok("Notifications marked as read"));
    }
}
