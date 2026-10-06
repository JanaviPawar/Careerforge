// src/main/java/com/careerforge/security/UserDetailsServiceImpl.java
package com.careerforge.security;

import com.careerforge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    /*
     * Spring Security calls loadUserByUsername() when it needs to verify who a
     * token belongs to. We tell it: "look up the user by email in our MySQL DB".
     * Our User class implements UserDetails, so we return it directly.
     */
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
