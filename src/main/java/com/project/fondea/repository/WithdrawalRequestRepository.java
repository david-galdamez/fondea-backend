package com.project.fondea.repository;

import com.project.fondea.model.WithdrawalRequest;
import com.project.fondea.model.enums.WithdrawalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface WithdrawalRequestRepository extends JpaRepository<WithdrawalRequest, UUID> {
    List<WithdrawalRequest> findByCreatorId(UUID creatorId);

    List<WithdrawalRequest> findByStatus(WithdrawalStatus status);

    @Query("SELECT COALESCE(SUM(w.netAmount), 0) FROM WithdrawalRequest w " +
            "WHERE w.creator.id = :creatorId " +
            "AND w.status = 'PAID' " +
            "AND CAST(w.paidAt AS date) = CURRENT_DATE")
    BigDecimal sumWithdrawnTodayByCreatorId(@Param("creatorId") Long creatorId);
}
