package com.example.morago_backend_nov24.repository;

import com.example.morago_backend_nov24.entity.Deposit;
import com.example.morago_backend_nov24.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, Long> {

    @Query("""
           select d from Deposit d
           join fetch d.userProfile u
           where u = :userProfile
           """)
    List<Deposit> findByUserProfile(@Param("userProfile") UserProfile userProfile);

    List<Deposit> findByStatus(String status);

    @Query("""
           select d from Deposit d
           join fetch d.userProfile u
           where u.id = :userId
           order by d.createdAt desc
           """)
    List<Deposit> findByUserProfileIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @Query("""
           select d from Deposit d
           join fetch d.userProfile u
           where d.status = :status
           order by d.createdAt desc
           """)
    List<Deposit> findByStatusOrderByCreatedAtDesc(@Param("status") String status);
}

