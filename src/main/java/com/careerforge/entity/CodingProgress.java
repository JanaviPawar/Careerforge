// src/main/java/com/careerforge/entity/CodingProgress.java
package com.careerforge.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "coding_progress")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CodingProgress {
    /*
     * @OneToOne: ONE user has exactly ONE coding progress record.
     * unique = true on the join column enforces this at the DB level.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private Integer leetcodeSolved   = 0;
    private Integer easyCount        = 0;
    private Integer mediumCount      = 0;
    private Integer hardCount        = 0;
    private Integer gfgScore         = 0;
    private Integer codeforcesRating = 0;
    private Integer currentStreak    = 0;
    private Integer maxStreak        = 0;

    // Stored as comma-separated: "DP,Trees,Graphs"
    private String weakTopics;
    private String strongTopics;

    private LocalDateTime lastUpdated;

    @PreUpdate protected void onUpdate() { lastUpdated = LocalDateTime.now(); }

    // OOP — Behaviour inside entity: readiness label computed from data
    @Transient  // NOT a DB column — computed on the fly
    public String getReadinessLevel() {
        if (leetcodeSolved == null) return "BEGINNER";
        if (leetcodeSolved >= 200) return "STRONG";
        if (leetcodeSolved >= 100) return "MODERATE";
        return "BEGINNER";
    }
}
