// src/main/java/com/careerforge/controller/CompanyController.java
package com.careerforge.controller;

import com.careerforge.dto.response.ApiResponse;
import com.careerforge.entity.*;
import com.careerforge.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Company>>> getAll(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Companies fetched",
                companyService.getAllCompanies(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Company>> getById(@PathVariable Long id) {
        // Result served from Redis cache if available
        return ResponseEntity.ok(ApiResponse.success("Company fetched",
                companyService.getById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Company>>> search(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Search results",
                companyService.searchCompanies(name, page, size)));
    }

    @GetMapping("/dream")
    public ResponseEntity<ApiResponse<List<Company>>> getDreamCompanies() {
        return ResponseEntity.ok(ApiResponse.success("Dream companies",
                companyService.getDreamCompanies()));
    }

    // Company-wise interview prep resources
    @GetMapping("/{id}/resources")
    public ResponseEntity<ApiResponse<List<Resource>>> getResources(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Resources fetched",
                companyService.getResourcesForCompany(id)));
    }

    // Admin can add companies and resources
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Company>> addCompany(@RequestBody Company company) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Company added",
                        companyService.addCompany(company)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Company>> updateCompany(
            @PathVariable Long id, @RequestBody Company company) {
        return ResponseEntity.ok(ApiResponse.success("Company updated",
                companyService.updateCompany(id, company)));
    }
}
