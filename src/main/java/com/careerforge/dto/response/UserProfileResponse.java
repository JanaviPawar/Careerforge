// src/main/java/com/careerforge/dto/response/UserProfileResponse.java
package com.careerforge.dto.response;

import com.careerforge.entity.User;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UserProfileResponse {
    /*
     * OOP — Abstraction:
     * The caller sees ONLY what they need. Password, isActive,
     * Spring Security internals are all hidden.
     *
     * Static factory method from() — converts Entity to DTO in one place.
     * If the response format changes, you change it here only.
     */
    private Long          id;
    private String        fullName;
    private String        email;
    private String        role;
    private String        college;
    private String        branch;
    private Integer       graduationYear;
    private Double        cgpa;
    private String        phone;
    private String        linkedinUrl;
    private String        githubUrl;
    private LocalDateTime createdAt;

    public static UserProfileResponse from(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .college(user.getCollege())
                .branch(user.getBranch())
                .graduationYear(user.getGraduationYear())
                .cgpa(user.getCgpa())
                .phone(user.getPhone())
                .linkedinUrl(user.getLinkedinUrl())
                .githubUrl(user.getGithubUrl())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
