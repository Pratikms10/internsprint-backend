package com.internsprint.service;

import com.internsprint.model.Internship;
import com.internsprint.model.Application;
import com.internsprint.repository.InternshipRepository;
import com.internsprint.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final InternshipRepository internshipRepository;
    private final ApplicationRepository applicationRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 9 * * *")
    public void sendDeadlineReminders() {
        System.out.println("Running deadline reminder job...");

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate threeDays = LocalDate.now().plusDays(3);

        List<Internship> internships = internshipRepository
                .findByStatusAndDeadlineBetween(Internship.Status.open, tomorrow, threeDays);

        for (Internship internship : internships) {
            List<Application> applications = applicationRepository
                    .findByInternshipId(internship.getId());

            for (Application app : applications) {
                try {
                    emailService.sendDeadlineReminderEmail(
                            app.getStudent().getEmail(),
                            app.getStudent().getName(),
                            internship.getTitle(),
                            internship.getDeadline() != null ? internship.getDeadline().toString() : ""
                    );
                } catch (Exception e) {
                    System.out.println("Reminder email failed: " + e.getMessage());
                }
            }
        }

        System.out.println("Deadline reminders sent for " + internships.size() + " internships");
    }
}