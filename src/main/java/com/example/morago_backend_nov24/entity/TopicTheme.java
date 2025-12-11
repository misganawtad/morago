package com.example.morago_backend_nov24.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "TOPIC_THEME")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopicTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String status;

    @Column(name = "icon_url")
    private String iconUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "THEME_CATEGORY",
            joinColumns = @JoinColumn(name = "theme_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<TopicCategory> categories = new HashSet<>();

    @ManyToMany(mappedBy = "themes", fetch = FetchType.LAZY)
    private Set<TranslatorProfile> translators = new HashSet<>();

    @OneToMany(mappedBy = "theme", fetch = FetchType.LAZY)
    private List<Call> calls;
}
