// src/main/java/com/careerforge/service/AptitudeService.java
package com.careerforge.service;

import com.careerforge.dto.request.AptitudeProgressRequest;
import com.careerforge.entity.AptitudeProgress;
import com.careerforge.entity.User;
import com.careerforge.exception.ResourceNotFoundException;
import com.careerforge.repository.AptitudeProgressRepository;
import com.careerforge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AptitudeService {

    private final AptitudeProgressRepository aptitudeRepository;
    private final UserRepository             userRepository;

    public AptitudeProgress getOrCreate(Long userId) {
        return aptitudeRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            AptitudeProgress ap = AptitudeProgress.builder()
                    .user(user).quantScore(0).verbalScore(0)
                    .logicalScore(0).diScore(0).practiceTests(0).build();
            return aptitudeRepository.save(ap);
        });
    }

    @Transactional
    public AptitudeProgress updateProgress(Long userId, AptitudeProgressRequest req) {
        AptitudeProgress ap = getOrCreate(userId);

        if (req.getQuantScore()    != null) ap.setQuantScore(req.getQuantScore());
        if (req.getVerbalScore()   != null) ap.setVerbalScore(req.getVerbalScore());
        if (req.getLogicalScore()  != null) ap.setLogicalScore(req.getLogicalScore());
        if (req.getDiScore()       != null) ap.setDiScore(req.getDiScore());
        if (req.getPracticeTests() != null) ap.setPracticeTests(req.getPracticeTests());
        if (req.getLastTestDate()  != null) ap.setLastTestDate(req.getLastTestDate());
        if (req.getNotes()         != null) ap.setNotes(req.getNotes());

        log.info("Aptitude updated for userId: {}", userId);
        return aptitudeRepository.save(ap);
    }

    public Map<String, Object> getAnalytics(Long userId) {
        AptitudeProgress ap = getOrCreate(userId);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("quantScore",          ap.getQuantScore());
        data.put("verbalScore",         ap.getVerbalScore());
        data.put("logicalScore",        ap.getLogicalScore());
        data.put("diScore",             ap.getDiScore());
        data.put("overallScore",        ap.getOverallScore());         // computed in entity
        data.put("weakestArea",         ap.getWeakestArea());          // computed in entity
        data.put("placementReadiness",  ap.getPlacementReadiness());   // computed in entity
        data.put("practiceTests",       ap.getPracticeTests());
        data.put("lastTestDate",        ap.getLastTestDate());
        return data;
    }
}
