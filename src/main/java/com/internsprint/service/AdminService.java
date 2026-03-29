package com.internsprint.service;

import com.internsprint.model.*;
import com.internsprint.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final InternshipRepository internshipRepository;
    private final NotificationRepository notificationRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public List<Internship> getAllInternships() {
        return internshipRepository.findAll();
    }

    @Transactional
    public Company verifyCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setIsVerified(true);
        company.setVerifiedAt(LocalDateTime.now());
        companyRepository.save(company);

        Notification n = new Notification();
        n.setUser(company.getUser());
        n.setMessage("Your company has been verified! You can now post internships.");
        n.setType(Notification.NotifType.general);
        notificationRepository.save(n);

        return company;
    }

    @Transactional
    public Company unverifyCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setIsVerified(false);
        company.setVerifiedAt(null);
        return companyRepository.save(company);
    }

    @Transactional
    public void deleteInternship(Long internshipId) {
        if (!internshipRepository.existsById(internshipId)) {
            throw new RuntimeException("Internship not found");
        }
        internshipRepository.deleteById(internshipId);
    }

    @Transactional
    public User deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(false);
        return userRepository.save(user);
    }
}
