package com.example.morago_backend_nov24.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CHARGE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Charge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfile userProfile;

    // Historical snapshot for audit
    @Column(name = "user_name")
    private String userName;

    private String label;

    // KRW, > 0
    private Long amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
