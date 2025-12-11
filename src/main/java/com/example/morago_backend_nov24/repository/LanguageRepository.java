package com.example.morago_backend_nov24.repository;

import com.example.morago_backend_nov24.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    Optional<Language> findByName(String name);

    @Query("""
        select l from Language l
        join fetch l.translators
        where l.id = :id
    """)
    Optional<Language> findByIdWithTranslators(@Param("id") Long id);


    @Query("""
        select distinct l from Language l
        join fetch l.translators
    """)
    List<Language> findAllWithTranslators();
}

