
package com.careerforge.entity;

import com.careerforge.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Application {
    /*
     * RELATIONSHIP EXPLAINED:
     *
     * @ManyToOne: "Many applications can belong to ONE user."
     *   ─ This puts a "user_id" foreign key column in the applications table.
     *
     * FetchType.LAZY: "Don't load the User object until someone calls .getUser()"
     *   ─ Saves a JOIN query on every Application fetch when user data is not needed.
     *
     * FetchType.EAGER: "Always load Company with every Application fetch."
     *   ─ We always want to know which company the application is for.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    private LocalDate appliedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    private LocalDate oaDate;
    private Integer   oaScore;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private String jobRole;
    private String jobLink;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist  protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate   protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
