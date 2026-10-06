// src/main/java/com/careerforge/controller/CodingProgressController.java
package com.careerforge.controller;

import com.careerforge.dto.request.CodingProgressRequest;
import com.careerforge.dto.response.ApiResponse;
import com.careerforge.entity.CodingProgress;
import com.careerforge.entity.User;
import com.careerforge.service.CodingProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/coding-progress")
@RequiredArgsConstructor
public class CodingProgressController {

    private final CodingProgressService codingProgressService;

    @GetMapping
    public ResponseEntity<ApiResponse<CodingProgress>> getProgress(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.success("Coding progress fetched",
                codingProgressService.getOrCreate(currentUser.getId())));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<CodingProgress>> updateProgress(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody CodingProgressRequest request) {
        CodingProgress updated = codingProgressService.updateProgress(currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Coding progress updated", updated));
    }

    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAnalytics(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.success("Analytics fetched",
                codingProgressService.getAnalytics(currentUser.getId())));
    }
}
