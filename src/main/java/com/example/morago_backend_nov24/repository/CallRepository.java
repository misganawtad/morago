package com.example.morago_backend_nov24.repository;

import com.example.morago_backend_nov24.entity.Call;
import com.example.morago_backend_nov24.entity.UserProfile;
import com.example.morago_backend_nov24.entity.TranslatorProfile;
import com.example.morago_backend_nov24.enums.CallStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CallRepository extends JpaRepository<Call, Long> {

    // Single objects – usually fine without fetch join
    List<Call> findByUserProfile(UserProfile userProfile);

    List<Call> findByTranslatorProfile(TranslatorProfile translatorProfile);

    // If you often need related info with status filters, you can also add fetch-join here
    @Query("select c from Call c " +
            "join fetch c.userProfile u " +
            "join fetch c.translatorProfile t " +
            "join fetch c.theme th " +
            "where c.status = :status")
    List<Call> findByStatus(@Param("status") CallStatus status);

    List<Call> findByStartedAtBetween(LocalDateTime start, LocalDateTime end);

    // 🔹 User’s calls with details (avoids N+1 when mapping to DTO)
    @Query("select c from Call c " +
            "join fetch c.userProfile u " +
            "join fetch c.translatorProfile t " +
            "join fetch c.theme th " +
            "where u.id = :userId " +
            "order by c.startedAt desc")
    List<Call> findByUserProfileIdOrderByStartedAtDesc(@Param("userId") Long userId);

    // 🔹 Translator’s calls with details
    @Query("select c from Call c " +
            "join fetch c.userProfile u " +
            "join fetch c.translatorProfile t " +
            "join fetch c.theme th " +
            "where t.id = :translatorId " +
            "order by c.startedAt desc")
    List<Call> findByTranslatorProfileIdOrderByStartedAtDesc(@Param("translatorId") Long translatorId);

    // 🔹 Translator’s calls with specific status (e.g. COMPLETED / PENDING)
    @Query("select c from Call c " +
            "join fetch c.userProfile u " +
            "join fetch c.translatorProfile t " +
            "join fetch c.theme th " +
            "where t.id = :translatorId and c.status = :status")
    List<Call> findByTranslatorProfileIdAndStatus(@Param("translatorId") Long translatorId,
                                                  @Param("status") CallStatus status);
}
