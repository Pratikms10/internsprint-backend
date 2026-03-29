package com.internsprint.repository;

import com.internsprint.model.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {

    List<Internship> findByStatus(Internship.Status status);

    List<Internship> findByStatusAndDomainContainingIgnoreCase(
            Internship.Status status, String domain);

    List<Internship> findByStatusAndLocationContainingIgnoreCase(
            Internship.Status status, String location);

    List<Internship> findByCompanyId(Long companyId);

    @Query("SELECT i FROM Internship i WHERE i.status = 'open' AND " +
           "(LOWER(i.title) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
           "LOWER(i.skillsRequired) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
           "LOWER(i.domain) LIKE LOWER(CONCAT('%',:keyword,'%')))")
    List<Internship> searchByKeyword(@Param("keyword") String keyword);
}
