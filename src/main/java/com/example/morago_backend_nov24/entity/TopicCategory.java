package com.example.morago_backend_nov24.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TOPIC_CATEGORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopicCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String status;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<TopicTheme> themes = new HashSet<>();
}
