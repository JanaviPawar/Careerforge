// src/main/java/com/careerforge/repository/CodingProgressRepository.java
package com.careerforge.repository;

import com.careerforge.entity.CodingProgress;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CodingProgressRepository extends JpaRepository<CodingProgress, Long> {
    Optional<CodingProgress> findByUserId(Long userId);
    boolean existsByUserId(Long userId);

    // Top solvers — used for leaderboard / dashboard
    @Query("SELECT cp FROM CodingProgress cp ORDER BY cp.leetcodeSolved DESC")
    List<CodingProgress> findTopSolvers(Pageable pageable);
}
