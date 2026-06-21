package com.internsprint.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "company_name", nullable = false, length = 200)
    private String companyName;

    @Column(length = 300)
    private String website;

    @Column(length = 100)
    private String industry;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Existing fields
    @Column(name = "gstin", length = 15)
    private String gstin;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    // New industry-standard fields
    @Column(name = "cin", length = 21)
    private String cin;

    @Column(name = "pan", length = 10)
    private String pan;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "linkedin_url", length = 300)
    private String linkedinUrl;

    @Column(name = "company_type", length = 50)
    private String companyType;

    @Column(name = "founded_year", length = 4)
    private String foundedYear;

    @Column(name = "company_size", length = 30)
    private String companySize;

    @Column(name = "tagline", length = 200)
    private String tagline;

    @Column(name = "perks", columnDefinition = "TEXT")
    private String perks;

    @PrePersist
    protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
