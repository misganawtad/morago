package com.example.morago_backend_nov24.repository;

import com.example.morago_backend_nov24.entity.Withdrawal;
import com.example.morago_backend_nov24.enums.WithdrawalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {

    List<Withdrawal> findByTranslatorProfileIdOrderByCreatedAtDesc(Long translatorId);

    List<Withdrawal> findByTranslatorProfileIdAndStatus(Long translatorId, WithdrawalStatus status);

    List<Withdrawal> findByStatusOrderByCreatedAtDesc(WithdrawalStatus status);


    @Query("""
           select w from Withdrawal w
           join fetch w.translatorProfile t
           where t.id = :translatorId
           order by w.createdAt desc
           """)
    List<Withdrawal> findByTranslatorProfileIdWithProfile(@Param("translatorId") Long translatorId);

    @Query("""
           select w from Withdrawal w
           join fetch w.translatorProfile t
           where t.id = :translatorId and w.status = :status
           order by w.createdAt desc
           """)
    List<Withdrawal> findByTranslatorProfileIdAndStatusWithProfile(
            @Param("translatorId") Long translatorId,
            @Param("status") WithdrawalStatus status
    );

    @Query("""
           select w from Withdrawal w
           join fetch w.translatorProfile t
           where w.status = :status
           order by w.createdAt desc
           """)
    List<Withdrawal> findByStatusOrderByCreatedAtDescWithProfile(@Param("status") WithdrawalStatus status);
}

