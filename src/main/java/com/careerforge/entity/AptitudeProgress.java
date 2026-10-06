// src/main/java/com/careerforge/entity/AptitudeProgress.java
package com.careerforge.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "aptitude_progress")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class AptitudeProgress {
    /*
     * Resume bullet: "aptitude tracking"
     * Tracks 4 aptitude areas: Quantitative, Verbal, Logical Reasoning,
     * Data Interpretation — the 4 sections in TCS NQT / campus drives.
     *
     * OOP — Encapsulation + Behaviour:
     * getOverallScore() and getWeakestArea() are BEHAVIOUR of this class.
     * Callers don't recompute this logic; they just call the method.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private Integer quantScore   = 0;   // Quantitative aptitude (0–100)
    private Integer verbalScore  = 0;   // Verbal ability (0–100)
    private Integer logicalScore = 0;   // Logical reasoning (0–100)
    private Integer diScore      = 0;   // Data Interpretation (0–100)

    private Integer practiceTests = 0;  // Number of mock tests taken
    private LocalDate lastTestDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDateTime lastUpdated;

    @PreUpdate
    protected void onUpdate() { lastUpdated = LocalDateTime.now(); }

    // ── Computed (transient) methods ─────────────────────────────────────

    @Transient
    public Integer getOverallScore() {
        return (quantScore + verbalScore + logicalScore + diScore) / 4;
    }

    @Transient
    public String getWeakestArea() {
        int min = Math.min(Math.min(quantScore, verbalScore),
                Math.min(logicalScore, diScore));
        if (min == quantScore)   return "Quantitative";
        if (min == verbalScore)  return "Verbal";
        if (min == logicalScore) return "Logical Reasoning";
        return "Data Interpretation";
    }

    @Transient
    public String getPlacementReadiness() {
        int overall = getOverallScore();
        if (overall >= 80) return "HIGH";
        if (overall >= 55) return "MEDIUM";
        return "NEEDS_WORK";
    }
}
