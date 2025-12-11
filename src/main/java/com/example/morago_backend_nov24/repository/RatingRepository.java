package com.example.morago_backend_nov24.repository;

import com.example.morago_backend_nov24.entity.Rating;
import com.example.morago_backend_nov24.entity.TranslatorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {


    List<Rating> findByRatedTranslator(TranslatorProfile translatorProfile);

    @Query("""
           select r from Rating r
           join fetch r.call c
           join fetch r.ratedByUser u
           join fetch r.ratedTranslator t
           where t = :translator
           """)
    List<Rating> findByRatedTranslatorWithDetails(@Param("translator") TranslatorProfile translatorProfile);
    Optional<Rating> findByCallId(Long callId);
}

