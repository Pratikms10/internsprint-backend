package com.internsprint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProfileResponse {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String companyName;
    private String website;
    private String industry;
    private String description;
    private Boolean isVerified;
    private String gstin;
    private String location;
    private String logoUrl;
}
