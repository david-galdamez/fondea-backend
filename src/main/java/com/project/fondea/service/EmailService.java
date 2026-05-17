package com.project.fondea.service;

import com.project.fondea.model.Campaign;
import com.project.fondea.model.CampaignUpdate;
import com.project.fondea.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmailService {

    public void sendUpdateToSponsors(List<User> sponsors, Campaign campaign,
                                     CampaignUpdate update) {
        // TODO: implementar con SendGrid o SMTP
        log.info("Enviando actualización '{}' a {} sponsors de la campaña '{}'",
                update.getTitle(), sponsors.size(), campaign.getTitle());
    }

    public void sendCampaignApproved(User creator, Campaign campaign) {
        log.info("Enviando email de aprobación a {} para campaña '{}'",
                creator.getEmail(), campaign.getTitle());
    }

    public void sendCampaignRejected(User creator, Campaign campaign) {
        log.info("Enviando email de rechazo a {} para campaña '{}'",
                creator.getEmail(), campaign.getTitle());
    }

    public void sendVerificationEmail(User user, String verificationCode) {
        log.info("Enviando código de verificación {} a {}",
                verificationCode, user.getEmail());
    }

    public void sendNearGoalNotification(User sponsor, Campaign campaign) {
        log.info("Notificando a {} que la campaña '{}' está cerca de su meta",
                sponsor.getEmail(), campaign.getTitle());
    }
}