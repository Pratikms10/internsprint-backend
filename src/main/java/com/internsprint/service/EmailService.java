package com.internsprint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.from.email}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public void sendPasswordResetEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("InternSprint - Reset Your Password");
        message.setText(
            "Hi,\n\n" +
            "You requested a password reset for your InternSprint account.\n\n" +
            "Click the link below to reset your password:\n" +
            frontendUrl + "/reset-password?token=" + token + "\n\n" +
            "This link expires in 1 hour.\n\n" +
            "If you didn't request this, please ignore this email.\n\n" +
            "— InternSprint Team"
        );
        mailSender.send(message);
    }

    public void sendWelcomeEmail(String toEmail, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Welcome to InternSprint!");
        message.setText(
            "Hi " + name + ",\n\n" +
            "Welcome to InternSprint! 🚀\n\n" +
            "Your account has been created successfully.\n" +
            "Start exploring internships at: " + frontendUrl + "\n\n" +
            "— InternSprint Team"
        );
        mailSender.send(message);
    }

    public void sendApplicationStatusEmail(String toEmail, String name,
            String internshipTitle, String status) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("InternSprint - Application Update: " + internshipTitle);
        message.setText(
            "Hi " + name + ",\n\n" +
            "Your application for \"" + internshipTitle + "\" has been updated.\n\n" +
            "New Status: " + status.replace("_", " ").toUpperCase() + "\n\n" +
            "View your applications: " + frontendUrl + "/student/applications\n\n" +
            "— InternSprint Team"
        );
        mailSender.send(message);
    }

    public void sendDeadlineReminderEmail(String toEmail, String name,
            String internshipTitle, String deadline) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("InternSprint - Application Deadline Reminder: " + internshipTitle);
        message.setText(
            "Hi " + name + ",\n\n" +
            "This is a reminder that the application deadline for \"" + internshipTitle + "\" is approaching.\n\n" +
            "Deadline: " + deadline + "\n\n" +
            "Don't miss out! View internship: " + frontendUrl + "/student/browse\n\n" +
            "— InternSprint Team"
        );
        mailSender.send(message);
    }
}