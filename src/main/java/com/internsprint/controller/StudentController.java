package com.internsprint.controller;

import com.internsprint.dto.ApiResponse;
import com.internsprint.dto.ApplicationRequest;
import com.internsprint.dto.StudentProfileRequest;
import com.internsprint.service.CloudinaryService;
import com.internsprint.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasRole('STUDENT')")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final CloudinaryService cloudinaryService;

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

    @PostMapping("/save/{internshipId}")
    public ResponseEntity<ApiResponse> saveInternship(
            @PathVariable Long internshipId, Authentication auth) {
        studentService.saveInternship(auth.getName(), internshipId);
        return ResponseEntity.ok(ApiResponse.ok("Internship saved"));
    }

    @DeleteMapping("/save/{internshipId}")
    public ResponseEntity<ApiResponse> unsaveInternship(
            @PathVariable Long internshipId, Authentication auth) {
        studentService.unsaveInternship(auth.getName(), internshipId);
        return ResponseEntity.ok(ApiResponse.ok("Internship removed from saved"));
    }

    @GetMapping("/saved")
    public ResponseEntity<ApiResponse> getSavedInternships(Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok("Saved internships",
                studentService.getSavedInternships(auth.getName())));
    }

    @DeleteMapping("/applications/{applicationId}/withdraw")
    public ResponseEntity<ApiResponse> withdrawApplication(
            @PathVariable Long applicationId, Authentication auth) {
        studentService.withdrawApplication(auth.getName(), applicationId);
        return ResponseEntity.ok(ApiResponse.ok("Application withdrawn"));
    }

    @PostMapping("/resume/upload")
    public ResponseEntity<ApiResponse> uploadResume(
            @RequestParam("file") MultipartFile file,
            Authentication auth) {
        try {
            String url = cloudinaryService.uploadResume(file);
            studentService.updateResumeUrl(auth.getName(), url);
            return ResponseEntity.ok(ApiResponse.ok("Resume uploaded", url));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.fail("Upload failed: " + e.getMessage()));
        }
    }
}