package com.internsprint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipResponse {
    private Long id;
    private String title;
    private String domain;
    private String location;
    private String skillsRequired;
    private String description;
    private String stipend;
    private String duration;
    private LocalDate deadline;
    private String status;
    private String companyName;
    private String companyIndustry;
    private Boolean companyVerified;
    private LocalDateTime createdAt;
}
