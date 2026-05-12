package com.project.fondea.service;

import com.project.fondea.exception.CampaignAlreadyReviewedException;
import com.project.fondea.exception.EntityNotFoundException;
import com.project.fondea.model.Campaign;
import com.project.fondea.model.FraudReport;
import com.project.fondea.model.WithdrawalRequest;
import com.project.fondea.model.enums.CampaignStatus;
import com.project.fondea.model.enums.FraudReportStatus;
import com.project.fondea.model.enums.WithdrawalStatus;
import com.project.fondea.repository.CampaignRepository;
import com.project.fondea.repository.FraudReportRepository;
import com.project.fondea.repository.WithdrawalRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final CampaignRepository campaignRepository;
    private final WithdrawalRequestRepository withdrawalRequestRepository;
    private final FraudReportRepository fraudReportRepository;

    public List<Campaign> getPendingCampaigns() {
        return campaignRepository.findByStatus(CampaignStatus.UNDER_REVIEW);
    }

    public Campaign approveCampaign(UUID campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if (campaign.getStatus() != CampaignStatus.UNDER_REVIEW) {
            throw new CampaignAlreadyReviewedException("La campaña ya fue revisada");
        }

        campaign.setStatus(CampaignStatus.ACTIVE);
        return campaignRepository.save(campaign);
    }

    public Campaign rejectCampaign(UUID campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if (campaign.getStatus() != CampaignStatus.UNDER_REVIEW) {
            throw new CampaignAlreadyReviewedException("La campaña ya fue revisada");
        }

        campaign.setStatus(CampaignStatus.FAILED);
        return campaignRepository.save(campaign);
    }

    public List<WithdrawalRequest> getPendingWithdrawals() {
        return withdrawalRequestRepository.findByStatus(WithdrawalStatus.PENDING);
    }

    public WithdrawalRequest approveWithdrawal(UUID withdrawalId) {
        WithdrawalRequest withdrawal = withdrawalRequestRepository.findById(withdrawalId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de retiro no encontrada"));

        if (withdrawal.getStatus() != WithdrawalStatus.PENDING) {
            throw new IllegalStateException("La solicitud de retiro ya fue revisada");
        }

        withdrawal.setStatus(WithdrawalStatus.APPROVED);
        return withdrawalRequestRepository.save(withdrawal);
    }

    public WithdrawalRequest rejectWithdrawal(UUID withdrawalId) {
        WithdrawalRequest withdrawal = withdrawalRequestRepository.findById(withdrawalId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de retiro no encontrada"));

        if (withdrawal.getStatus() != WithdrawalStatus.PENDING) {
            throw new IllegalStateException("La solicitud de retiro ya fue revisada");
        }

        withdrawal.setStatus(WithdrawalStatus.REJECTED);
        return withdrawalRequestRepository.save(withdrawal);
    }

    public List<FraudReport> getPendingFraudReports() {
        return fraudReportRepository.findByStatus(FraudReportStatus.PENDING);
    }

    public FraudReport reviewFraudReport(UUID reportId) {
        FraudReport report = fraudReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Reporte de fraude no encontrado"));

        if (report.getStatus() != FraudReportStatus.PENDING) {
            throw new IllegalStateException("El reporte de fraude ya fue revisado");
        }

        report.setStatus(FraudReportStatus.REVIEWED);
        return fraudReportRepository.save(report);
    }
}