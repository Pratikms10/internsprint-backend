package com.internsprint.dto;

import lombok.Data;

@Data
public class StudentProfileRequest {
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
