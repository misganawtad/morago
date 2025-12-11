package com.example.morago_backend_nov24.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TRANSLATOR_PROFILE")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranslatorProfile extends User {

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "topik_level")
    private String topikLevel;

    private Long balance;

    @Column(name = "topik_certificate_url")
    private String topikCertificateUrl;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @ManyToMany
    @JoinTable(
            name = "TRANSLATOR_LANGUAGE",
            joinColumns = @JoinColumn(name = "translator_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    @JsonIgnoreProperties({"translators"})  // ✅ Don't include translators in languages
    private Set<Language> languages = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "TRANSLATOR_THEME",
            joinColumns = @JoinColumn(name = "translator_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id")
    )
    @JsonIgnoreProperties({"translators"})  // ✅ Don't include translators in themes
    private Set<TopicTheme> themes = new HashSet<>();

    @OneToMany(mappedBy = "translatorProfile")
    @JsonIgnore  // ✅ Don't serialize files
    private List<TranslatorFile> files;

    @OneToMany(mappedBy = "translatorProfile")
    @JsonIgnore  // ✅ Don't serialize calls
    private List<Call> calls;

    @OneToMany(mappedBy = "ratedTranslator")
    @JsonIgnore  // ✅ Don't serialize ratings
    private List<Rating> ratingsReceived;

    @OneToMany(mappedBy = "translatorProfile")
    @JsonIgnore  // ✅ Don't serialize withdrawals
    private List<Withdrawal> withdrawals;
}