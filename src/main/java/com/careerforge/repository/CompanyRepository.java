// src/main/java/com/careerforge/repository/CompanyRepository.java
package com.careerforge.repository;

import com.careerforge.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    // Case-insensitive search — e.g. "tcs" finds "TCS"
    Page<Company> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Company> findByIsDreamTrue();
    List<Company> findByIndustry(String industry);
}
