// src/main/java/com/careerforge/entity/InterviewRecord.java
package com.careerforge.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_records")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class InterviewRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private Application application;

    @Column(nullable = false)
    private LocalDateTime interviewDate;

    private Integer round = 1;  // Round 1, 2, 3 …

    @Enumerated(EnumType.STRING)
    private RoundType roundType;

    private Integer durationMins;

    @Enumerated(EnumType.STRING)
    private Mode mode = Mode.ONLINE;

    @Enumerated(EnumType.STRING)
    private Outcome outcome = Outcome.PENDING;

    @Column(name = "rating")
    private Integer rating;     // Self-rating 1–5

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(columnDefinition = "TEXT")
    private String questionsAsked;  // Store as JSON string: ["What is HashMap?", ...]

    private String topicsCovered;
    private LocalDateTime createdAt;

    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }

    public enum RoundType { TECHNICAL, HR, MANAGERIAL, CODING, GROUP_DISCUSSION }
    public enum Mode      { ONLINE, OFFLINE, HYBRID }
    public enum Outcome   { PASSED, FAILED, PENDING }
}
