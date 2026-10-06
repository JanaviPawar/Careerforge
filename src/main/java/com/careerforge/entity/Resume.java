// src/main/java/com/careerforge/entity/Resume.java
package com.careerforge.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resumes")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String fileName;

    @Column(nullable = false, length = 500)
    private String s3Key;   // The path/key inside S3 bucket — never changes

    @Column(nullable = false, length = 1000)
    private String s3Url;   // Pre-signed URL — regenerated on each download request

    private Integer version   = 1;
    private Boolean isPrimary = false;

    private LocalDateTime uploadDate;

    @PrePersist
    protected void onCreate() { uploadDate = LocalDateTime.now(); }
}
