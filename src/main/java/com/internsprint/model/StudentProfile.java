package com.internsprint.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 200)
    private String college;

    @Column(length = 100)
    private String degree;

    private Integer year;

    @Column
    private Double cgpa;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(name = "resume_url", length = 500)
    private String resumeUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 300)
    private String linkedin;

    @Column(length = 300)
    private String github;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}