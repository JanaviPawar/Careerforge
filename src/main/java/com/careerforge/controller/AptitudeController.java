// src/main/java/com/careerforge/controller/AptitudeController.java
package com.careerforge.controller;

import com.careerforge.dto.request.AptitudeProgressRequest;
import com.careerforge.dto.response.ApiResponse;
import com.careerforge.entity.AptitudeProgress;
import com.careerforge.entity.User;
import com.careerforge.service.AptitudeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/aptitude")
@RequiredArgsConstructor
public class AptitudeController {

    private final AptitudeService aptitudeService;

    @GetMapping
    public ResponseEntity<ApiResponse<AptitudeProgress>> getProgress(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.success("Aptitude progress fetched",
                aptitudeService.getOrCreate(currentUser.getId())));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<AptitudeProgress>> updateProgress(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody AptitudeProgressRequest request) {
        AptitudeProgress updated = aptitudeService.updateProgress(currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Aptitude updated", updated));
    }

    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAnalytics(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.success("Aptitude analytics fetched",
                aptitudeService.getAnalytics(currentUser.getId())));
    }
}