// src/main/java/com/careerforge/repository/ResourceRepository.java
package com.careerforge.repository;

import com.careerforge.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByCompanyId(Long companyId);
    List<Resource> findByTagsContainingIgnoreCase(String tag);
}
