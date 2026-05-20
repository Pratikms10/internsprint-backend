package com.internsprint.dto;

import lombok.Data;

@Data
public class CompanyProfileRequest {
    private String companyName;
    private String website;
    private String industry;
    private String description;
    private String gstin;
    private String location;
    private String logoUrl;
}
