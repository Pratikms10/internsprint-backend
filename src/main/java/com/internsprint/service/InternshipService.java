package com.internsprint.service;

import com.internsprint.dto.InternshipResponse;
import com.internsprint.model.Internship;
import com.internsprint.repository.InternshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InternshipService {

    private final InternshipRepository internshipRepository;

    public List<InternshipResponse> search(String keyword,
                                           String domain,
                                           String location) {
        List<Internship> results;

        if (keyword != null && !keyword.isBlank()) {
            results = internshipRepository.searchByKeyword(keyword.trim());
        } else if (domain != null && !domain.isBlank()) {
            results = internshipRepository
                    .findByStatusAndDomainContainingIgnoreCase(
                            Internship.Status.open, domain);
        } else if (location != null && !location.isBlank()) {
            results = internshipRepository
                    .findByStatusAndLocationContainingIgnoreCase(
                            Internship.Status.open, location);
        } else {
            results = internshipRepository.findByStatus(Internship.Status.open);
        }

        return results.stream()
                .map(InternshipResponse::from)
                .collect(Collectors.toList());
    }

    public InternshipResponse getById(Long id) {
        Internship internship = internshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Internship not found"));
        return InternshipResponse.from(internship);
    }
}