// src/main/java/com/careerforge/service/AuthService.java
package com.careerforge.service;

import com.careerforge.dto.request.LoginRequest;
import com.careerforge.dto.request.SignupRequest;
import com.careerforge.dto.response.AuthResponse;
import com.careerforge.entity.User;
import com.careerforge.enums.Role;
import com.careerforge.exception.ResourceNotFoundException;
import com.careerforge.repository.UserRepository;
import com.careerforge.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository        userRepository;
    private final PasswordEncoder       passwordEncoder;
    private final JwtUtil               jwtUtil;
    private final AuthenticationManager authManager;

    public AuthResponse signup(SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail()))
            throw new IllegalArgumentException("Email already registered: " + req.getEmail());

        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword())) // ← BCrypt hash
                .role(Role.STUDENT)
                .college(req.getCollege())
                .branch(req.getBranch())
                .graduationYear(req.getGraduationYear())
                .cgpa(req.getCgpa())
                .phone(req.getPhone())
                .isActive(true)
                .build();

        User saved = userRepository.save(user);
        log.info("New user registered: {}", saved.getEmail());

        return buildAuthResponse(saved, "Account created successfully!");
    }

    public AuthResponse login(LoginRequest req) {
        // AuthenticationManager internally calls UserDetailsService + BCrypt.verify()
        // Throws BadCredentialsException if wrong email/password
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        log.info("User logged in: {}", user.getEmail());
        return buildAuthResponse(user, "Login successful!");
    }

    private AuthResponse buildAuthResponse(User user, String message) {
        return AuthResponse.builder()
                .token(jwtUtil.generateToken(user))
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .message(message)
                .build();
    }
}
