package com.example.morago_backend_nov24.entity;

import com.example.morago_backend_nov24.enums.CallStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CALL_RECORD")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Call {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = false)
    @JsonIgnoreProperties({"calls", "deposits", "charges", "ratingsGiven", "passwordHash", "roles"})
    private UserProfile userProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "translator_profile_id", nullable = false)
    @JsonIgnoreProperties({"calls", "files", "withdrawals", "ratingsReceived", "passwordHash", "roles"})
    private TranslatorProfile translatorProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", nullable = false)
    @JsonIgnoreProperties({"translators", "calls"})
    private TopicTheme theme;


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CallStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    private Long sum;

    @OneToOne(mappedBy = "call", fetch = FetchType.LAZY)
    @JsonIgnore
    private Rating rating;


    public String getStatus() {
        return status != null ? status.name() : null;
    }

    public void setStatus(String status) {
        this.status = CallStatus.valueOf(status);
    }

    public void setStatus(CallStatus status) {
        this.status = status;
    }
}
