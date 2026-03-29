package com.internsprint.service;

import com.internsprint.dto.*;
import com.internsprint.model.Company;
import com.internsprint.model.StudentProfile;
import com.internsprint.model.User;
import com.internsprint.repository.CompanyRepository;
import com.internsprint.repository.StudentProfileRepository;
import com.internsprint.repository.UserRepository;
import com.internsprint.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered: " + request.getEmail());
        }

        if (request.getRole() == User.Role.admin) {
            throw new RuntimeException("Cannot register as admin");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user = userRepository.save(user);

        if (request.getRole() == User.Role.student) {
            StudentProfile profile = new StudentProfile();
            profile.setUser(user);
            studentProfileRepository.save(profile);

        } else if (request.getRole() == User.Role.company) {
            if (request.getCompanyName() == null || request.getCompanyName().isBlank()) {
                throw new RuntimeException("Company name is required");
            }
            Company company = new Company();
            company.setUser(user);
            company.setCompanyName(request.getCompanyName());
            company.setWebsite(request.getWebsite());
            company.setIndustry(request.getIndustry());
            companyRepository.save(company);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, user.getRole().name(),
                user.getId(), user.getName(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, user.getRole().name(),
                user.getId(), user.getName(), user.getEmail());
    }
}
