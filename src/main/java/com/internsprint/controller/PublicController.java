package com.internsprint.controller;

import com.internsprint.dto.ApiResponse;
import com.internsprint.model.Company;
import com.internsprint.model.Internship;
import com.internsprint.repository.CompanyRepository;
import com.internsprint.repository.InternshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final CompanyRepository companyRepository;
    private final InternshipRepository internshipRepository;

    @GetMapping("/company/{id}")
    public ResponseEntity<ApiResponse> getCompanyProfile(@PathVariable Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        List<Internship> internships = internshipRepository.findByCompanyId(id);

        Map<String, Object> data = new HashMap<>();
        data.put("id", company.getId());
        data.put("companyName", company.getCompanyName());
        data.put("industry", company.getIndustry());
        data.put("location", company.getLocation());
        data.put("website", company.getWebsite());
        data.put("description", company.getDescription());
        data.put("logoUrl", company.getLogoUrl());
        data.put("isVerified", company.getIsVerified());
        data.put("gstin", company.getGstin() != null ? "Registered" : null); // mask actual GSTIN
        data.put("totalInternships", internships.size());
        data.put("openInternships", internships.stream()
                .filter(i -> i.getStatus() == Internship.Status.open).count());
        data.put("internships", internships.stream()
                .filter(i -> i.getStatus() == Internship.Status.open)
                .map(i -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", i.getId());
                    m.put("title", i.getTitle());
                    m.put("domain", i.getDomain());
                    m.put("location", i.getLocation());
                    m.put("stipend", i.getStipend());
                    m.put("duration", i.getDuration());
                    m.put("skillsRequired", i.getSkillsRequired());
                    m.put("deadline", i.getDeadline());
                    return m;
                }).collect(Collectors.toList()));

        return ResponseEntity.ok(ApiResponse.ok("Company profile fetched", data));
    }

    @GetMapping("/companies")
    public ResponseEntity<ApiResponse> getAllPublicCompanies() {
        List<Company> companies = companyRepository.findAll()
                .stream()
                .filter(c -> Boolean.TRUE.equals(c.getIsVerified()))
                .collect(Collectors.toList());

        List<Map<String, Object>> result = companies.stream().map(c -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", c.getId());
            m.put("companyName", c.getCompanyName());
            m.put("industry", c.getIndustry());
            m.put("location", c.getLocation());
            m.put("website", c.getWebsite());
            m.put("logoUrl", c.getLogoUrl());
            m.put("isVerified", c.getIsVerified());
            m.put("openInternships", internshipRepository.findByCompanyId(c.getId())
                    .stream().filter(i -> i.getStatus() == Internship.Status.open).count());
            return m;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.ok("Companies fetched", result));
    }
}
