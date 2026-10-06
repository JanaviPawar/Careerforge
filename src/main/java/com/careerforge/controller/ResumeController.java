// src/main/java/com/careerforge/controller/ResumeController.java
package com.careerforge.controller;

import com.careerforge.dto.response.ApiResponse;
import com.careerforge.entity.Resume;
import com.careerforge.entity.User;
import com.careerforge.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Resume>> upload(
            @AuthenticationPrincipal User currentUser,
            @RequestParam("file") MultipartFile file) {
        /*
         * In Postman:
         *   Method: POST
         *   URL:    /api/resumes/upload
         *   Body:   form-data → key="file" (type=File) → select your PDF
         *   Header: Authorization: Bearer <your-token>
         */
        Resume resume = resumeService.upload(currentUser.getId(), file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resume uploaded", resume));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Resume>>> getMyResumes(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.success("Resumes fetched",
                resumeService.getMyResumes(currentUser.getId())));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<ApiResponse<Map<String, String>>> getDownloadUrl(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id) {
        String url = resumeService.getDownloadUrl(id, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Download link generated (valid 1 hour)",
                Map.of("downloadUrl", url, "expiresIn", "1 hour")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id) {
        resumeService.delete(id, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Resume deleted from S3 and database", null));
    }
}
