// src/main/java/com/careerforge/dto/response/ApiResponse.java
package com.careerforge.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    /*
     * Generic class <T> — one class works for ALL response types.
     * ApiResponse<UserProfileResponse>
     * ApiResponse<List<CompanyDto>>
     * ApiResponse<Map<String, Long>>
     * This is compile-time POLYMORPHISM (Generics).
     *
     * Every API response looks the same to the client:
     * { "success": true, "message": "...", "data": {...}, "timestamp": "..." }
     * Consistency = professional API design.
     */

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true).message(message)
                .data(data).timestamp(LocalDateTime.now()).build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false).message(message)
                .timestamp(LocalDateTime.now()).build();
    }
}
