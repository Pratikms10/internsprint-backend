package com.internsprint.service;

import com.internsprint.dto.*;
import com.internsprint.model.*;
import com.internsprint.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final InternshipRepository internshipRepository;
    private final ApplicationRepository applicationRepository;
    private final NotificationRepository notificationRepository;

    public StudentProfileResponse getProfile(String email) {
        User user = findUser(email);
        StudentProfile profile = studentProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return toProfileResponse(user, profile);
    }

    @Transactional
    public StudentProfileResponse updateProfile(String email,
                                                StudentProfileRequest request) {
        User user = findUser(email);
        StudentProfile profile = studentProfileRepository
                .findByUserId(user.getId())
                .orElse(new StudentProfile());

        profile.setUser(user);
        profile.setCollege(request.getCollege());
        profile.setDegree(request.getDegree());
        profile.setYear(request.getYear());
        profile.setCgpa(request.getCgpa());
        profile.setSkills(request.getSkills());
        profile.setBio(request.getBio());
        profile.setLinkedin(request.getLinkedin());
        profile.setGithub(request.getGithub());
        profile.setResumeUrl(request.getResumeUrl());
        studentProfileRepository.save(profile);

        return toProfileResponse(user, profile);
    }

    @Transactional
    public ApplicationResponse apply(String email, Long internshipId,
                                     ApplicationRequest request) {
        User student = findUser(email);

        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found"));

        if (internship.getStatus() == Internship.Status.closed) {
            throw new RuntimeException("This internship is no longer accepting applications");
        }

        if (applicationRepository.existsByStudentIdAndInternshipId(
                student.getId(), internshipId)) {
            throw new RuntimeException("You have already applied to this internship");
        }

        Application application = new Application();
        application.setStudent(student);
        application.setInternship(internship);
        application.setCoverLetter(request.getCoverLetter());
        application = applicationRepository.save(application);

        saveNotification(student,
                "Application submitted for: " + internship.getTitle(),
                Notification.NotifType.status_update);

        return toApplicationResponse(application);
    }

    public List<ApplicationResponse> getMyApplications(String email) {
        User student = findUser(email);
        return applicationRepository.findByStudentId(student.getId())
                .stream()
                .map(this::toApplicationResponse)
                .collect(Collectors.toList());
    }

    public List<Notification> getNotifications(String email) {
        User user = findUser(email);
        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    @Transactional
    public void markNotificationsRead(String email) {
        User user = findUser(email);
        List<Notification> unread = notificationRepository
                .findByUserIdAndIsRead(user.getId(), false);
        unread.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(unread);
    }

    // ── helpers ──────────────────────────────────────

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void saveNotification(User user, String message,
                                  Notification.NotifType type) {
        Notification n = new Notification();
        n.setUser(user);
        n.setMessage(message);
        n.setType(type);
        notificationRepository.save(n);
    }

    private StudentProfileResponse toProfileResponse(User user,
                                                     StudentProfile p) {
        return new StudentProfileResponse(
                user.getId(), user.getName(), user.getEmail(),
                p.getCollege(), p.getDegree(), p.getYear(), p.getCgpa(),
                p.getSkills(), p.getBio(), p.getLinkedin(),
                p.getGithub(), p.getResumeUrl()
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
