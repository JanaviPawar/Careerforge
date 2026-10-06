// src/main/java/com/careerforge/dto/request/CodingProgressRequest.java
package com.careerforge.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;
import java.util.List;

@Data
public class CodingProgressRequest {
    @Min(0) private Integer leetcodeSolved;
    @Min(0) private Integer easyCount;
    @Min(0) private Integer mediumCount;
    @Min(0) private Integer hardCount;
    @Min(0) private Integer gfgScore;
    @Min(0) private Integer codeforcesRating;
    @Min(0) private Integer currentStreak;
    @Min(0) private Integer maxStreak;
    private List<String> weakTopics;   // ["DP", "Trees", "Graphs"]
    private List<String> strongTopics; // ["Arrays", "Hashing"]
}
