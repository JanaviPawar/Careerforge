package com.careerforge.entity;
import com.careerforge.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;    // ALWAYS stored as bcrypt hash — NEVER plain text

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String college;
    private String branch;
    private Integer graduationYear;
    private Double cgpa;
    private String phone;
    private String linkedinUrl;
    private String githubUrl;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    // OOP — Abstraction: timestamp logic hidden inside the entity itself
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ── UserDetails methods (required by Spring Security) ─────────────────

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Tells Spring Security: "this user has ROLE_STUDENT" or "ROLE_ADMIN"
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() { return email; } // We use email as login ID

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return isActive; }
    @Override
    public String getPassword() {
        return password;
    }
}
