// src/main/java/com/careerforge/service/CompanyService.java
package com.careerforge.service;

import com.careerforge.entity.Company;
import com.careerforge.entity.Resource;
import com.careerforge.exception.ResourceNotFoundException;
import com.careerforge.repository.CompanyRepository;
import com.careerforge.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository  companyRepository;
    private final ResourceRepository resourceRepository;

    @Cacheable(value = "companies", key = "#id")
    /*
     * REDIS CACHING FLOW:
     * 1st call: Redis miss → hit MySQL → store in Redis with 10-min TTL → return
     * 2nd call: Redis hit  → return directly (no MySQL hit)
     *
     * WHY THIS MATTERS: Company data is requested by every student.
     * Without caching: 100 students × 5 page loads = 500 DB queries.
     * With caching:    1 DB query + 499 Redis lookups (microseconds).
     */
    public Company getById(Long id) {
        log.info("DB fetch for company id={}", id);
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + id));
    }

    @Cacheable(value = "dreamCompanies")
    public List<Company> getDreamCompanies() {
        return companyRepository.findByIsDreamTrue();
    }

    public Page<Company> searchCompanies(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return companyRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Company> getAllCompanies(int page, int size) {
        return companyRepository.findAll(
                PageRequest.of(page, size, Sort.by("name").ascending()));
    }

    @CacheEvict(value = {"companies", "dreamCompanies"}, allEntries = true)
    public Company addCompany(Company company) {
        return companyRepository.save(company);
    }

    @CacheEvict(value = "companies", key = "#id")
    public Company updateCompany(Long id, Company updated) {
        Company existing = getById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPackageLpa(updated.getPackageLpa());
        existing.setIsDream(updated.getIsDream());
        existing.setDifficultyLevel(updated.getDifficultyLevel());
        return companyRepository.save(existing);
    }

    // Company-wise resources — the "interview preparation" feature
    public List<Resource> getResourcesForCompany(Long companyId) {
        return resourceRepository.findByCompanyId(companyId);
    }

    public Resource addResource(Resource resource) {
        return resourceRepository.save(resource);
    }
}
