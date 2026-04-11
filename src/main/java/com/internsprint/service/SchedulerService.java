package com.internsprint.service;

import com.internsprint.model.Internship;
import com.internsprint.model.Application;
import com.internsprint.repository.InternshipRepository;
import com.internsprint.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerService {

    private final InternshipRepository internshipRepository;
    private final ApplicationRepository applicationRepository;
    private final EmailService emailService;

    // Runs every day at 9:00 AM
    @Scheduled(cron = "0 0 9 * * *")
    public void sendDeadlineReminders() {
        System.out.println("Running deadline reminder job...");

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate threeDays = LocalDate.now().plusDays(3);

        // Get internships with deadline tomorrow or in 3 days
        List<Internship> internships = internshipRepository
            .findByStatusAndDeadlineBetween(
                Internship.Status.open,
                tomorrow.toString(),
                threeDays.toString()
            );

        for (Internship internship : internships) {
            // Get all applicants
            List<Application> applications = applicationRepository
                .findByInternshipId(internship.getId());

            for (Application app : applications) {
                try {
                    emailService.sendDeadlineReminderEmail(
                        app.getStudent().getEmail(),
                        app.getStudent().getName(),
                        internship.getTitle(),
                        internship.getDeadline()
                    );
                } catch (Exception e) {
                    System.out.println("Reminder email failed: " + e.getMessage());
                }
            }
        }

        System.out.println("Deadline reminders sent for " + internships.size() + " internships");
    }
}