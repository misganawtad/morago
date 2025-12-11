package com.example.morago_backend_nov24.repository;

import com.example.morago_backend_nov24.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u left join fetch u.roles where u.phone = :phone")
    Optional<User> findByPhoneWithRoles(@Param("phone") String phone);

    Optional<User> findByPhone(String phone);
    boolean existsByPhone(String phone);
    boolean existsByRolesName(String name);
}


