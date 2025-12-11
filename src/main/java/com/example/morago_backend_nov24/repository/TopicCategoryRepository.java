package com.example.morago_backend_nov24.repository;

import com.example.morago_backend_nov24.entity.TopicCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicCategoryRepository extends JpaRepository<TopicCategory, Long> {

    List<TopicCategory> findByStatus(String status);
}
