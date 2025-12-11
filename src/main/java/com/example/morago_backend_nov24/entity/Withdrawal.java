package com.example.morago_backend_nov24.entity;

import com.example.morago_backend_nov24.enums.WithdrawalStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "WITHDRAWAL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 👈 make it LAZY
    @JoinColumn(name = "translator_profile_id", nullable = false)
    private TranslatorProfile translatorProfile;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account")
    private String bankAccount;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private WithdrawalStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
