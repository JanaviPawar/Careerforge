// src/main/java/com/careerforge/service/CodingProgressService.java
package com.careerforge.service;

import com.careerforge.dto.request.CodingProgressRequest;
import com.careerforge.entity.CodingProgress;
import com.careerforge.entity.User;
import com.careerforge.exception.ResourceNotFoundException;
import com.careerforge.repository.CodingProgressRepository;
import com.careerforge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodingProgressService {

    private final CodingProgressRepository codingProgressRepository;
    private final UserRepository           userRepository;

    @Cacheable(value = "codingProgress", key = "#userId")
    public CodingProgress getOrCreate(Long userId) {
        return codingProgressRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            CodingProgress cp = CodingProgress.builder()
                    .user(user).leetcodeSolved(0).easyCount(0)
                    .mediumCount(0).hardCount(0).gfgScore(0)
                    .codeforcesRating(0).currentStreak(0).maxStreak(0).build();
            return codingProgressRepository.save(cp);
        });
    }

    @Transactional
    @CacheEvict(value = {"codingProgress", "dashboard"}, key = "#userId")
    public CodingProgress updateProgress(Long userId, CodingProgressRequest req) {
        CodingProgress cp = getOrCreate(userId);

        if (req.getLeetcodeSolved()   != null) cp.setLeetcodeSolved(req.getLeetcodeSolved());
        if (req.getEasyCount()        != null) cp.setEasyCount(req.getEasyCount());
        if (req.getMediumCount()      != null) cp.setMediumCount(req.getMediumCount());
        if (req.getHardCount()        != null) cp.setHardCount(req.getHardCount());
        if (req.getGfgScore()         != null) cp.setGfgScore(req.getGfgScore());
        if (req.getCodeforcesRating() != null) cp.setCodeforcesRating(req.getCodeforcesRating());
        if (req.getCurrentStreak()    != null) {
            cp.setCurrentStreak(req.getCurrentStreak());
            if (req.getCurrentStreak() > cp.getMaxStreak())
                cp.setMaxStreak(req.getCurrentStreak()); // auto-update max
        }
        if (req.getWeakTopics()       != null)
            cp.setWeakTopics(String.join(",", req.getWeakTopics()));
        if (req.getStrongTopics()     != null)
            cp.setStrongTopics(String.join(",", req.getStrongTopics()));

        return codingProgressRepository.save(cp);
    }

    public Map<String, Object> getAnalytics(Long userId) {
        CodingProgress cp = getOrCreate(userId);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("leetcodeSolved",   cp.getLeetcodeSolved());
        data.put("easyCount",        cp.getEasyCount());
        data.put("mediumCount",      cp.getMediumCount());
        data.put("hardCount",        cp.getHardCount());
        data.put("gfgScore",         cp.getGfgScore());
        data.put("codeforcesRating", cp.getCodeforcesRating());
        data.put("currentStreak",    cp.getCurrentStreak());
        data.put("maxStreak",        cp.getMaxStreak());
        data.put("codingReadiness",  cp.getReadinessLevel()); // computed in entity
        data.put("weakTopics",  cp.getWeakTopics()   != null
                ? List.of(cp.getWeakTopics().split(","))   : List.of());
        data.put("strongTopics",cp.getStrongTopics() != null
                ? List.of(cp.getStrongTopics().split(",")) : List.of());
        return data;
    }
}
