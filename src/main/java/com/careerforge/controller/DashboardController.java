// src/main/java/com/careerforge/controller/DashboardController.java
package com.careerforge.controller;

import com.careerforge.dto.response.ApiResponse;
import com.careerforge.entity.User;
import com.careerforge.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboard(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.success("Dashboard loaded",
                dashboardService.getDashboard(currentUser.getId())));
    }
}
