// src/main/java/com/careerforge/repository/InterviewRepository.java
package com.careerforge.repository;

import com.careerforge.entity.InterviewRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<InterviewRecord, Long> {
    List<InterviewRecord> findByUserIdOrderByInterviewDateDesc(Long userId);
    List<InterviewRecord> findByUserIdAndCompanyId(Long userId, Long companyId);

    @Query("SELECT AVG(ir.rating) FROM InterviewRecord ir WHERE ir.user.id = :userId")
    Double findAverageRatingByUserId(Long userId);

    @Query("SELECT ir.outcome, COUNT(ir) FROM InterviewRecord ir WHERE ir.user.id = :userId GROUP BY ir.outcome")
    List<Object[]> countByOutcomeForUser(Long userId);
}
