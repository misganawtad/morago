package com.example.morago_backend_nov24.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "RATING")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "call_id", nullable = false, unique = true)
    private Call call;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rated_by_user_id", nullable = false)
    private UserProfile ratedByUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rated_translator_id", nullable = false)
    private TranslatorProfile ratedTranslator;

    private Integer score;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
