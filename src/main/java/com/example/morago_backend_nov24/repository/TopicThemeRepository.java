package com.example.morago_backend_nov24.repository;

import com.example.morago_backend_nov24.entity.TopicTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicThemeRepository extends JpaRepository<TopicTheme, Long> {

    List<TopicTheme> findByStatus(String status);

}
