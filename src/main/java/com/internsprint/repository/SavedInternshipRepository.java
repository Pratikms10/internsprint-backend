package com.internsprint.repository;

import com.internsprint.model.SavedInternship;
import com.internsprint.model.User;
import com.internsprint.model.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SavedInternshipRepository extends JpaRepository<SavedInternship, Long> {
    List<SavedInternship> findByStudent(User student);
    Optional<SavedInternship> findByStudentAndInternship(User student, Internship internship);
    boolean existsByStudentAndInternship(User student, Internship internship);
    void deleteByStudentAndInternship(User student, Internship internship);
}