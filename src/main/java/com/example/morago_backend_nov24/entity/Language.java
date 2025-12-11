package com.example.morago_backend_nov24.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "LANGUAGE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "languages", fetch = FetchType.LAZY)
    private Set<TranslatorProfile> translators = new HashSet<>();
}
