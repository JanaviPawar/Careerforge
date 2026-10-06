// src/main/java/com/careerforge/controller/ApplicationController.java
package com.careerforge.controller;

import com.careerforge.dto.request.ApplicationRequest;
import com.careerforge.dto.response.ApiResponse;
import com.careerforge.entity.*;
import com.careerforge.enums.ApplicationStatus;
import com.careerforge.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApiResponse<Application>> create(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody ApplicationRequest request) {
        Application app = applicationService.create(currentUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Application added", app));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Application>>> getMyApplications(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Applications fetched",
                applicationService.getUserApplications(currentUser.getId(), page, size)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Application>> updateStatus(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id,
            @RequestParam ApplicationStatus status) {
        return ResponseEntity.ok(ApiResponse.success("Status updated to " + status,
                applicationService.updateStatus(id, currentUser.getId(), status)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id) {
        applicationService.deleteApplication(id, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Application deleted", null));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getStats(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.success("Stats fetched",
                applicationService.getStatusStats(currentUser.getId())));
    }
}
