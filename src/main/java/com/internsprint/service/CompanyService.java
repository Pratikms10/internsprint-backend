package com.internsprint.service;

import com.internsprint.dto.*;
import com.internsprint.model.*;
import com.internsprint.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final InternshipRepository internshipRepository;
    private final ApplicationRepository applicationRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public InternshipResponse postInternship(String email,
                                             InternshipRequest request) {
        Company company = findCompany(email);

        if (!company.getIsVerified()) {
            throw new RuntimeException(
                    "Your company is pending verification. " +
                    "Please wait for admin approval before posting internships.");
        }

        Internship internship = new Internship();
        internship.setCompany(company);
        internship.setTitle(request.getTitle());
        internship.setDomain(request.getDomain());
        internship.setLocation(request.getLocation());
        internship.setSkillsRequired(request.getSkillsRequired());
        internship.setDescription(request.getDescription());
        internship.setStipend(request.getStipend());
        internship.setDuration(request.getDuration());
        internship.setDeadline(request.getDeadline());
        internship = internshipRepository.save(internship);

        return toInternshipResponse(internship);
    }

    public List<InternshipResponse> getMyInternships(String email) {
        Company company = findCompany(email);
        return internshipRepository.findByCompanyId(company.getId())
                .stream()
                .map(this::toInternshipResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public InternshipResponse closeInternship(String email, Long internshipId) {
        Company company = findCompany(email);
        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found"));

        if (!internship.getCompany().getId().equals(company.getId())) {
            throw new RuntimeException("Access denied");
        }

        internship.setStatus(Internship.Status.closed);
        internshipRepository.save(internship);
        return toInternshipResponse(internship);
    }

    public List<ApplicationResponse> getApplications(String email,
                                                     Long internshipId) {
        Company company = findCompany(email);
        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found"));

        if (!internship.getCompany().getId().equals(company.getId())) {
            throw new RuntimeException("Access denied");
        }

        return applicationRepository.findByInternshipId(internshipId)
                .stream()
                .map(this::toApplicationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ApplicationResponse updateApplicationStatus(String email,
                                                       Long applicationId,
                                                       String newStatus) {
        Company company = findCompany(email);
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getInternship().getCompany().getId()
                .equals(company.getId())) {
            throw new RuntimeException("Access denied");
        }

        application.setStatus(Application.AppStatus.valueOf(newStatus));
        applicationRepository.save(application);

        Notification n = new Notification();
        n.setUser(application.getStudent());
        n.setMessage("Your application for "
                + application.getInternship().getTitle()
                + " at " + company.getCompanyName()
                + " is now: " + newStatus.replace("_", " "));
        n.setType(Notification.NotifType.status_update);
        notificationRepository.save(n);

        return toApplicationResponse(application);
    }

    @Transactional
    public ApplicationResponse scheduleInterview(String email,
                                                 Long applicationId,
                                                 LocalDateTime interviewDate) {
        Company company = findCompany(email);
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getInternship().getCompany().getId()
                .equals(company.getId())) {
            throw new RuntimeException("Access denied");
        }

        application.setInterviewDate(interviewDate);
        application.setStatus(Application.AppStatus.interview_scheduled);
        applicationRepository.save(application);

        Notification n = new Notification();
        n.setUser(application.getStudent());
        n.setMessage("Interview scheduled for "
                + application.getInternship().getTitle()
                + " on " + interviewDate);
        n.setType(Notification.NotifType.interview);
        notificationRepository.save(n);

        return toApplicationResponse(application);
    }

    // ── helpers ──────────────────────────────────────

    private Company findCompany(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return companyRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Company profile not found"));
    }

    private InternshipResponse toInternshipResponse(Internship i) {
        Company c = i.getCompany();
        return new InternshipResponse(
                i.getId(), i.getTitle(), i.getDomain(), i.getLocation(),
                i.getSkillsRequired(), i.getDescription(), i.getStipend(),
                i.getDuration(), i.getDeadline(), i.getStatus().name(),
                c.getCompanyName(), c.getIndustry(),
                c.getIsVerified(), i.getCreatedAt()
        );
    }

    private ApplicationResponse toApplicationResponse(Application a) {
        return new ApplicationResponse(
                a.getId(),
                a.getInternship().getId(),
                a.getInternship().getTitle(),
                a.getInternship().getCompany().getCompanyName(),
                a.getStatus(),
                a.getCoverLetter(),
                a.getInterviewDate(),
                a.getAppliedAt(),
                a.getUpdatedAt()
        );
    }
}
