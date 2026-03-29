package com.internsprint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileResponse {
    private Long userId;
    private String name;
    private String email;
    private String college;
    private String degree;
    private Integer year;
    private Double cgpa;
    private String skills;
    private String bio;
    private String linkedin;
    private String github;
    private String resumeUrl;
}
