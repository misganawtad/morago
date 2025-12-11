package com.example.morago_backend_nov24.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSLATOR_FILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranslatorFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "translator_profile_id", nullable = false)
    private TranslatorProfile translatorProfile;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;
}

