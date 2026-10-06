// src/main/java/com/careerforge/repository/AptitudeProgressRepository.java
package com.careerforge.repository;

import com.careerforge.entity.AptitudeProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AptitudeProgressRepository extends JpaRepository<AptitudeProgress, Long> {
    Optional<AptitudeProgress> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
