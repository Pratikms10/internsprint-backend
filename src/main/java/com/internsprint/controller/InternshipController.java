package com.internsprint.controller;

import com.internsprint.dto.ApiResponse;
import com.internsprint.service.InternshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internships")
@RequiredArgsConstructor
public class InternshipController {

    private final InternshipService internshipService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String domain,
            @RequestParam(required = false) String location) {
        return ResponseEntity.ok(
                ApiResponse.ok("Internships fetched",
                        internshipService.search(keyword, domain, location)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok("Internship fetched",
                        internshipService.getById(id)));
    }
}
