// src/main/java/com/careerforge/dto/request/SignupRequest.java
package com.careerforge.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignupRequest {
    // @NotBlank = cannot be null or empty/whitespace
    // @Email    = must look like a valid email address
    // If validation fails, Spring returns 400 automatically before hitting your code

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private String college;
    private String branch;
    private Integer graduationYear;

    @DecimalMin("0.0") @DecimalMax("10.0")
    private Double cgpa;

    private String phone;
}
