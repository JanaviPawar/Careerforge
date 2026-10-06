package com.careerforge.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Company implements Serializable {
    /*
     * WHY Serializable?
     * Redis converts objects to bytes before storing them.
     * Serializable tells Java "this object CAN be converted to bytes".
     * Without it, Redis caching of Company objects will throw an error.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    private String industry;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String website;
    private Double packageLpa;
    private Integer bondYears        = 0;
    private Integer interviewRounds  = 3;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel = DifficultyLevel.MEDIUM;

    private Boolean isDream = false;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    // Inner enum — grouped inside Company because it belongs to Company context
    public enum DifficultyLevel { EASY, MEDIUM, HARD }
}