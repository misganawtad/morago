package com.example.morago_backend_nov24.repository;

import com.example.morago_backend_nov24.entity.Charge;
import com.example.morago_backend_nov24.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ChargeRepository extends JpaRepository<Charge, Long> {

    @Query("""
           select c from Charge c
           join fetch c.userProfile u
           where u = :userProfile
           """)
    List<Charge> findByUserProfile(@Param("userProfile") UserProfile userProfile);

    @Query("""
           select c from Charge c
           join fetch c.userProfile u
           where u = :userProfile
             and c.createdAt between :start and :end
           """)
    List<Charge> findByUserProfileAndCreatedAtBetween(
            @Param("userProfile") UserProfile userProfile,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
           select c from Charge c
           join fetch c.userProfile u
           where u.id = :userId
           order by c.createdAt desc
           """)
    List<Charge> findByUserProfileIdOrderByCreatedAtDesc(@Param("userId") Long userId);
}


