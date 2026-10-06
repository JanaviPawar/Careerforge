// src/main/java/com/careerforge/controller/InterviewController.java
package com.careerforge.controller;

import com.careerforge.dto.request.InterviewRequest;
import com.careerforge.dto.response.ApiResponse;
import com.careerforge.entity.*;
import com.careerforge.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<InterviewRecord>> schedule(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody InterviewRequest request) {
        InterviewRecord record = interviewService.schedule(currentUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Interview scheduled", record));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InterviewRecord>>> getAll(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.success("Interviews fetched",
                interviewService.getMyInterviews(currentUser.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InterviewRecord>> getOne(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Interview fetched",
                interviewService.getById(id, currentUser.getId())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InterviewRecord>> update(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id,
            @RequestBody InterviewRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Interview updated",
                interviewService.updateFeedback(id, currentUser.getId(), request)));
    }

    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAnalytics(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.success("Interview analytics",
                interviewService.getInterviewAnalytics(currentUser.getId())));
    }
}
