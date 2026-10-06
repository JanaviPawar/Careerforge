// src/main/java/com/careerforge/service/ApplicationService.java
package com.careerforge.service;

import com.careerforge.dto.request.ApplicationRequest;
import com.careerforge.entity.*;
import com.careerforge.enums.ApplicationStatus;
import com.careerforge.exception.*;
import com.careerforge.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository        userRepository;
    private final CompanyRepository     companyRepository;

    @Transactional
    public Application create(Long userId, ApplicationRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Company company = companyRepository.findById(req.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Company not found: " + req.getCompanyId()));

        Application app = Application.builder()
                .user(user).company(company)
                .appliedDate(req.getAppliedDate())
                .status(req.getStatus())
                .jobRole(req.getJobRole())
                .jobLink(req.getJobLink())
                .oaDate(req.getOaDate())
                .oaScore(req.getOaScore())
                .notes(req.getNotes())
                .build();
        return applicationRepository.save(app);
    }

    public Page<Application> getUserApplications(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return applicationRepository.findByUserId(userId, pageable);
    }

    @Transactional
    public Application updateStatus(Long appId, Long userId, ApplicationStatus status) {
        Application app = applicationRepository.findById(appId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        if (!app.getUser().getId().equals(userId))
            throw new UnauthorizedException("You cannot modify this application");
        app.setStatus(status);
        return applicationRepository.save(app);
    }

    public void deleteApplication(Long appId, Long userId) {
        Application app = applicationRepository.findById(appId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        if (!app.getUser().getId().equals(userId))
            throw new UnauthorizedException("You cannot delete this application");
        applicationRepository.delete(app);
    }

    // Analytics — used by dashboard
    public Map<String, Long> getStatusStats(Long userId) {
        return applicationRepository.countByUserIdGroupByStatus(userId)
                .stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(),
                        row -> (Long) row[1]));
    }
}
