package com.example.morago_backend_nov24.config;

import com.example.morago_backend_nov24.entity.Role;
import com.example.morago_backend_nov24.entity.User;
import com.example.morago_backend_nov24.repository.RoleRepository;
import com.example.morago_backend_nov24.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdmin() {
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new IllegalStateException("Role ADMIN not found"));

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Role USER not found"));

        String adminPhone = "01021942209";
        User admin = userRepository.findByPhoneWithRoles(adminPhone)
                .orElseGet(User::new);

        admin.setPhone(adminPhone);
        admin.setPasswordHash(passwordEncoder.encode("123456"));
        admin.setStatus("ACTIVE");

        if (admin.getCreatedAt() == null) {
            admin.setCreatedAt(LocalDateTime.now());
        }

        Set<Role> roles = new HashSet<>(admin.getRoles());
        roles.add(adminRole);
        roles.add(userRole);
        admin.setRoles(roles);

        userRepository.save(admin);

        System.out.println("⚠️ Default ADMIN ensured: phone=" + adminPhone + ", password=123456");
    }
}
