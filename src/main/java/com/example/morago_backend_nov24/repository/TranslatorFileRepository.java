package com.example.morago_backend_nov24.repository;

import com.example.morago_backend_nov24.entity.TranslatorFile;
import com.example.morago_backend_nov24.entity.TranslatorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TranslatorFileRepository extends JpaRepository<TranslatorFile, Long> {

    @Query("""
           select f from TranslatorFile f
           join fetch f.translatorProfile t
           where t = :translatorProfile
           """)
    List<TranslatorFile> findByTranslatorProfile(@Param("translatorProfile") TranslatorProfile translatorProfile);
}


