package com.internsprint.dto;

import com.internsprint.model.Application;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {
    private Long id;
    private Long internshipId;
    private String internshipTitle;
    private String companyName;
    private Application.AppStatus status;
    private String coverLetter;
    private LocalDateTime interviewDate;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}
