package com.example.morago_backend_nov24.repository;

import com.example.morago_backend_nov24.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
