// src/main/java/com/careerforge/service/InterviewService.java
package com.careerforge.service;

import com.careerforge.dto.request.InterviewRequest;
import com.careerforge.entity.*;
import com.careerforge.exception.*;
import com.careerforge.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository   interviewRepository;
    private final UserRepository        userRepository;
    private final CompanyRepository     companyRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional
    public InterviewRecord schedule(Long userId, InterviewRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Company company = companyRepository.findById(req.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        Application application = null;
        if (req.getApplicationId() != null) {
            application = applicationRepository.findById(req.getApplicationId()).orElse(null);
        }

        InterviewRecord record = InterviewRecord.builder()
                .user(user).company(company).application(application)
                .interviewDate(req.getInterviewDate())
                .round(req.getRound())
                .roundType(req.getRoundType())
                .durationMins(req.getDurationMins())
                .mode(req.getMode())
                .outcome(req.getOutcome())
                .rating(req.getRating())
                .feedback(req.getFeedback())
                .questionsAsked(req.getQuestionsAsked())
                .topicsCovered(req.getTopicsCovered())
                .build();

        return interviewRepository.save(record);
    }

    public List<InterviewRecord> getMyInterviews(Long userId) {
        return interviewRepository.findByUserIdOrderByInterviewDateDesc(userId);
    }

    public InterviewRecord getById(Long id, Long userId) {
        InterviewRecord record = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview record not found"));
        if (!record.getUser().getId().equals(userId))
            throw new UnauthorizedException("Access denied");
        return record;
    }

    @Transactional
    public InterviewRecord updateFeedback(Long id, Long userId, InterviewRequest req) {
        InterviewRecord record = getById(id, userId);
        if (req.getOutcome()        != null) record.setOutcome(req.getOutcome());
        if (req.getRating()         != null) record.setRating(req.getRating());
        if (req.getFeedback()       != null) record.setFeedback(req.getFeedback());
        if (req.getQuestionsAsked() != null) record.setQuestionsAsked(req.getQuestionsAsked());
        if (req.getTopicsCovered()  != null) record.setTopicsCovered(req.getTopicsCovered());
        return interviewRepository.save(record);
    }

    public Map<String, Object> getInterviewAnalytics(Long userId) {
        Double avgRating = interviewRepository.findAverageRatingByUserId(userId);
        List<Object[]> outcomeCounts = interviewRepository.countByOutcomeForUser(userId);

        Map<String, Object> analytics = new LinkedHashMap<>();
        analytics.put("averageRating", avgRating != null ? avgRating : 0.0);

        Map<String, Long> outcomes = outcomeCounts.stream()
                .collect(Collectors.toMap(r -> r[0].toString(), r -> (Long) r[1]));
        analytics.put("outcomeBreakdown", outcomes);
        analytics.put("totalInterviews", outcomeCounts.stream().mapToLong(r -> (Long)r[1]).sum());
        return analytics;
    }
}
