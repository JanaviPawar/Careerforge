// src/main/java/com/careerforge/repository/ApplicationRepository.java
package com.careerforge.repository;

import com.careerforge.entity.Application;
import com.careerforge.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // Paginated list of a user's applications (resume bullet: "progress tracking")
    Page<Application> findByUserId(Long userId, Pageable pageable);

    // Filter by status (e.g. show only SELECTED applications)
    List<Application> findByUserIdAndStatus(Long userId, ApplicationStatus status);

    // Count per status — used by the dashboard
    @Query("SELECT a.status, COUNT(a) FROM Application a WHERE a.user.id = :userId GROUP BY a.status")
    List<Object[]> countByUserIdGroupByStatus(Long userId);

    long countByUserIdAndStatus(Long userId, ApplicationStatus status);
}