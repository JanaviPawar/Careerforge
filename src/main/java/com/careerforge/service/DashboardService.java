// src/main/java/com/careerforge/service/DashboardService.java
package com.careerforge.service;

import com.careerforge.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ApplicationRepository    applicationRepository;
    private final InterviewRepository      interviewRepository;
    private final CodingProgressRepository codingProgressRepository;
    private final AptitudeProgressRepository aptitudeProgressRepository;

    @Cacheable(value = "dashboard", key = "#userId")
    /*
     * This method hits 4 tables. Caching it in Redis means:
     * - 1st visit: all 4 DB queries → result cached
     * - Subsequent visits: 1 Redis lookup (microseconds)
     * Cache is evicted when user updates coding/aptitude progress.
     */
    public Map<String, Object> getDashboard(Long userId) {
        Map<String, Object> dashboard = new LinkedHashMap<>();

        // ── Application Stats ──────────────────────────────────────
        List<Object[]> appStats = applicationRepository.countByUserIdGroupByStatus(userId);
        Map<String, Long> appMap = appStats.stream()
                .collect(Collectors.toMap(r -> r[0].toString(), r -> (Long) r[1]));

        long total    = appMap.values().stream().mapToLong(Long::longValue).sum();
        long selected = appMap.getOrDefault("SELECTED", 0L);
        double successRate = total > 0 ? (double) selected / total * 100 : 0;

        dashboard.put("totalApplications", total);
        dashboard.put("applicationBreakdown", appMap);
        dashboard.put("selectedCount", selected);
        dashboard.put("successRate", String.format("%.1f%%", successRate));

        // ── Interview Stats ─────────────────────────────────────────
        Double avgRating = interviewRepository.findAverageRatingByUserId(userId);
        dashboard.put("averageInterviewRating", avgRating != null ? avgRating : 0.0);

        // ── Coding Progress ─────────────────────────────────────────
        codingProgressRepository.findByUserId(userId).ifPresent(cp -> {
            dashboard.put("leetcodeSolved",  cp.getLeetcodeSolved());
            dashboard.put("currentStreak",   cp.getCurrentStreak());
            dashboard.put("codingReadiness", cp.getReadinessLevel());
            dashboard.put("weakTopics", cp.getWeakTopics() != null
                    ? List.of(cp.getWeakTopics().split(",")) : List.of());
        });

        // ── Aptitude Overview ───────────────────────────────────────
        aptitudeProgressRepository.findByUserId(userId).ifPresent(ap -> {
            dashboard.put("aptitudeOverallScore",   ap.getOverallScore());
            dashboard.put("aptitudeWeakestArea",    ap.getWeakestArea());
            dashboard.put("placementReadiness",     ap.getPlacementReadiness());
        });

        return dashboard;
    }
}
