// src/main/java/com/careerforge/service/UserService.java
package com.careerforge.service;

import com.careerforge.dto.request.UpdateProfileRequest;
import com.careerforge.dto.response.UserProfileResponse;
import com.careerforge.entity.User;
import com.careerforge.exception.ResourceNotFoundException;
import com.careerforge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    /*
     * OOP — Single Responsibility Principle (SOLID):
     * UserService handles ONLY profile operations.
     * AuthService handles login/signup.
     * Each class has ONE reason to change.
     */
    private final UserRepository userRepository;

    @Cacheable(value = "userProfile", key = "#userId")
    public UserProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        return UserProfileResponse.from(user);
    }

    @Transactional
    @CacheEvict(value = "userProfile", key = "#userId")
    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // Partial update — only apply fields that were sent (not null)
        if (req.getFullName()       != null) user.setFullName(req.getFullName());
        if (req.getCollege()        != null) user.setCollege(req.getCollege());
        if (req.getBranch()         != null) user.setBranch(req.getBranch());
        if (req.getGraduationYear() != null) user.setGraduationYear(req.getGraduationYear());
        if (req.getCgpa()           != null) user.setCgpa(req.getCgpa());
        if (req.getPhone()          != null) user.setPhone(req.getPhone());
        if (req.getLinkedinUrl()    != null) user.setLinkedinUrl(req.getLinkedinUrl());
        if (req.getGithubUrl()      != null) user.setGithubUrl(req.getGithubUrl());

        User saved = userRepository.save(user);
        log.info("Profile updated: {}", saved.getEmail());
        return UserProfileResponse.from(saved);
    }

    public List<UserProfileResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserProfileResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        user.setActive(false);
        userRepository.save(user);
    }
}
