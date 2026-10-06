// src/main/java/com/careerforge/dto/request/AptitudeProgressRequest.java
package com.careerforge.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AptitudeProgressRequest {
    @Min(0) @Max(100) private Integer quantScore;
    @Min(0) @Max(100) private Integer verbalScore;
    @Min(0) @Max(100) private Integer logicalScore;
    @Min(0) @Max(100) private Integer diScore;
    @Min(0)           private Integer practiceTests;
    private LocalDate lastTestDate;
    private String    notes;
}
