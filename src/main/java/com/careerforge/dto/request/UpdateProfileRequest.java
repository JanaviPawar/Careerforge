// src/main/java/com/careerforge/dto/request/UpdateProfileRequest.java
package com.careerforge.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    /*
     * OOP — Encapsulation:
     * Email and role are NOT here — they cannot be changed via this DTO.
     * The DTO acts as a controlled gate — only what we ALLOW can be updated.
     */
    @Size(min = 2, max = 100)
    private String fullName;
    private String college;
    private String branch;

    @Min(2020) @Max(2030)
    private Integer graduationYear;

    @DecimalMin("0.0") @DecimalMax("10.0")
    private Double cgpa;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phone;

    @Size(max = 300)
    private String linkedinUrl;

    @Size(max = 300)
    private String githubUrl;
}
