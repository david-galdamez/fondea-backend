package com.project.fondea.service;

import com.project.fondea.dto.withdrawal.CreateWithdrawalRequest;
import com.project.fondea.dto.withdrawal.WithdrawalCreatedDto;
import com.project.fondea.dto.withdrawal.WithdrawalDto;
import com.project.fondea.dto.withdrawal.WithdrawalMapper;
import com.project.fondea.exception.CampaignGoalNotReachedException;
import com.project.fondea.exception.EntityNotFoundException;
import com.project.fondea.exception.UnauthorizedActionException;
import com.project.fondea.exception.WithdrawalLimitExceededException;
import com.project.fondea.model.WithdrawalRequest;
import com.project.fondea.model.enums.CampaignStatus;
import com.project.fondea.model.enums.WithdrawalStatus;
import com.project.fondea.repository.CampaignRepository;
import com.project.fondea.repository.CreatorProfileRepository;
import com.project.fondea.repository.UserRepository;
import com.project.fondea.repository.WithdrawalRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.project.fondea.exception.BusinessRuleException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WithdrawalService {
    private final WithdrawalRequestRepository withdrawalRequestRepository;
    private final CreatorProfileRepository creatorProfileRepository;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    private static final BigDecimal COMMISSION_RATE = new BigDecimal("0.05");

    public WithdrawalCreatedDto requestWithdrawal(UUID creatorId,
                                                  CreateWithdrawalRequest request) {
        var profile = creatorProfileRepository.findByUserId(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("Perfil de creador no encontrado"));

        var campaign = campaignRepository.findById(request.campaignId())
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if (!campaign.getCreator().getId().equals(creatorId)) {
            throw new UnauthorizedActionException("No eres el creador de esta campaña");
        }

        if (campaign.getStatus() != CampaignStatus.SUCCESSFUL) {
            throw new CampaignGoalNotReachedException(request.campaignId());
        }

        if (profile.getIsNewCreator()) {
            var withdrawnToday = withdrawalRequestRepository
                    .sumWithdrawnTodayByCreatorId(creatorId);

            if (withdrawnToday.add(request.grossAmount())
                    .compareTo(profile.getDailyWithdrawalLimit()) > 0) {
                throw new WithdrawalLimitExceededException(profile.getDailyWithdrawalLimit());
            }
        }

        var creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("Creador no encontrado"));

        var commission = request.grossAmount().multiply(COMMISSION_RATE);
        var netAmount = request.grossAmount().subtract(commission);

        var withdrawal = WithdrawalRequest.builder()
                .creator(creator)
                .campaign(campaign)
                .grossAmount(request.grossAmount())
                .commissionAmount(commission)
                .netAmount(netAmount)
                .status(WithdrawalStatus.PENDING)
                .build();

        return WithdrawalMapper.toCreated(withdrawalRequestRepository.save(withdrawal));
    }

    public List<WithdrawalDto> getMyWithdrawals(UUID creatorId) {
        return withdrawalRequestRepository.findByCreatorId(creatorId)
                .stream()
                .map(WithdrawalMapper::toDto)
                .toList();
    }

    public WithdrawalDto approve(UUID withdrawalId) {
        var withdrawal = findById(withdrawalId);

        if (withdrawal.getStatus() != WithdrawalStatus.PENDING) {
            throw new BusinessRuleException("Solo se pueden aprobar retiros pendientes");
        }

        withdrawal.setStatus(WithdrawalStatus.APPROVED);
        withdrawal.setPaidAt(LocalDateTime.now());

        return WithdrawalMapper.toDto(withdrawalRequestRepository.save(withdrawal));
    }

    public WithdrawalDto reject(UUID withdrawalId) {
        var withdrawal = findById(withdrawalId);

        if (withdrawal.getStatus() != WithdrawalStatus.PENDING) {
            throw new BusinessRuleException("Solo se pueden rechazar retiros pendientes");
        }

        withdrawal.setStatus(WithdrawalStatus.REJECTED);

        return WithdrawalMapper.toDto(withdrawalRequestRepository.save(withdrawal));
    }

    public List<WithdrawalDto> getPending() {
        return withdrawalRequestRepository.findByStatus(WithdrawalStatus.PENDING)
                .stream()
                .map(WithdrawalMapper::toDto)
                .toList();
    }

    private WithdrawalRequest findById(UUID withdrawalId) {
        return withdrawalRequestRepository.findById(withdrawalId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de retiro no encontrada"));
    }
}
