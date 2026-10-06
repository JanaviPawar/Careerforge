// src/main/java/com/careerforge/dto/request/ApplicationRequest.java
package com.careerforge.dto.request;

import com.careerforge.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ApplicationRequest {
    @NotNull(message = "Company ID is required")
    private Long companyId;
    private LocalDate appliedDate;
    private ApplicationStatus status = ApplicationStatus.APPLIED;
    private String jobRole;
    private String jobLink;
    private LocalDate oaDate;
    private Integer oaScore;
    private String notes;
}
