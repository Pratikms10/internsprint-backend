package com.internsprint.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.from.email}")
    private String fromEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    private void sendEmail(String to, String subject, String html) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> body = Map.of(
            "from", fromEmail,
            "to", new String[]{to},
            "subject", subject,
            "html", html
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.resend.com/emails", request, String.class
            );
            System.out.println("Email sent: " + response.getStatusCode());
        } catch (Exception e) {
            System.err.println("Email failed: " + e.getMessage());
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public void sendPasswordResetEmail(String toEmail, String token) {
        String resetLink = frontendUrl + "/reset-password?token=" + token;
        sendEmail(toEmail, "InternSprint - Reset Your Password",
            "<h2>Reset Your Password</h2>" +
            "<p>Click the link below to reset your password:</p>" +
            "<a href='" + resetLink + "' style='background:#3b82f6;color:white;padding:12px 24px;border-radius:8px;text-decoration:none;display:inline-block;'>Reset Password</a>" +
            "<p>This link expires in 1 hour.</p>" +
            "<p>If you didn't request this, ignore this email.</p>"
        );
    }

    public void sendWelcomeEmail(String toEmail, String name) {
        sendEmail(toEmail, "Welcome to InternSprint!",
            "<h2>Welcome, " + name + "! 🚀</h2>" +
            "<p>Your InternSprint account has been created successfully.</p>" +
            "<a href='" + frontendUrl + "' style='background:#3b82f6;color:white;padding:12px 24px;border-radius:8px;text-decoration:none;display:inline-block;'>Start Exploring</a>"
        );
    }

    public void sendApplicationStatusEmail(String toEmail, String name,
            String internshipTitle, String status) {
        sendEmail(toEmail, "Application Update: " + internshipTitle,
            "<h2>Hi " + name + ",</h2>" +
            "<p>Your application for <strong>" + internshipTitle + "</strong> has been updated.</p>" +
            "<p>New Status: <strong>" + status.replace("_", " ").toUpperCase() + "</strong></p>" +
            "<a href='" + frontendUrl + "/student/applications' style='background:#3b82f6;color:white;padding:12px 24px;border-radius:8px;text-decoration:none;display:inline-block;'>View Applications</a>"
        );
    }

    public void sendDeadlineReminderEmail(String toEmail, String name,
            String internshipTitle, String deadline) {
        sendEmail(toEmail, "Deadline Reminder: " + internshipTitle,
            "<h2>Hi " + name + ",</h2>" +
            "<p>The deadline for <strong>" + internshipTitle + "</strong> is approaching.</p>" +
            "<p>Deadline: <strong>" + deadline + "</strong></p>" +
            "<a href='" + frontendUrl + "/student/browse' style='background:#3b82f6;color:white;padding:12px 24px;border-radius:8px;text-decoration:none;display:inline-block;'>View Internship</a>"
        );
    }
}