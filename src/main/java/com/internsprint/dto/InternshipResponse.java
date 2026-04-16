package com.internsprint.dto;

import com.internsprint.model.Internship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private String deadline;
    private String status;
    private String companyName;
    private String companyIndustry;
    private Boolean companyVerified;
    private LocalDateTime createdAt;

    public static InternshipResponse from(Internship i) {
        return new InternshipResponse(
            i.getId(),
            i.getTitle(),
            i.getDomain(),
            i.getLocation(),
            i.getSkillsRequired(),
            i.getDescription(),
            i.getStipend(),
            i.getDuration(),
            i.getDeadline() != null ? i.getDeadline().toString() : null,
            i.getStatus().name(),
            i.getCompany().getCompanyName(),
            i.getCompany().getIndustry(),
            i.getCompany().getIsVerified(),
            i.getCreatedAt()
        );
    }
}