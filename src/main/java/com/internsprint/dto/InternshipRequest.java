package com.internsprint.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class InternshipRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String domain;
    private String location;
    private String skillsRequired;
    private String description;
    private String stipend;
    private String duration;
    private LocalDate deadline;
}
