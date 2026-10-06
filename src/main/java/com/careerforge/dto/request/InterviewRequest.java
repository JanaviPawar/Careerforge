// src/main/java/com/careerforge/dto/request/InterviewRequest.java
package com.careerforge.dto.request;

import com.careerforge.entity.InterviewRecord;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InterviewRequest {
    @NotNull private Long companyId;
    private Long applicationId;     // Optional — link to an existing application

    @NotNull private LocalDateTime interviewDate;
    private Integer round = 1;

    private InterviewRecord.RoundType roundType;
    private Integer durationMins;
    private InterviewRecord.Mode mode = InterviewRecord.Mode.ONLINE;
    private InterviewRecord.Outcome outcome = InterviewRecord.Outcome.PENDING;

    @Min(1) @Max(5)
    private Integer rating;

    private String feedback;
    private String questionsAsked;  // JSON array string: ["Q1","Q2"]
    private String topicsCovered;
}
