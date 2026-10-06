// src/main/java/com/careerforge/dto/response/AuthResponse.java
package com.careerforge.dto.response;

import lombok.*;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long   userId;
    private String email;
    private String fullName;
    private String role;
    private String message;
}
