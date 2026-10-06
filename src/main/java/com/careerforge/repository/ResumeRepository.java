// src/main/java/com/careerforge/repository/ResumeRepository.java
package com.careerforge.repository;

import com.careerforge.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    // Latest resume first
    List<Resume> findByUserIdOrderByUploadDateDesc(Long userId);
    Optional<Resume> findByUserIdAndIsPrimaryTrue(Long userId);
}
