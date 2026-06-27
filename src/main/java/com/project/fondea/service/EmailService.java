package com.project.fondea.service;

import com.project.fondea.model.Campaign;
import com.project.fondea.model.CampaignUpdate;
import com.project.fondea.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String from;

    public void sendUpdateToSponsors(List<User> sponsors, Campaign campaign, CampaignUpdate update) {
        sponsors.forEach(sponsor -> sendEmail(
                sponsor.getEmail(),
                "Nueva actualizacion de la campana: " + campaign.getTitle(),
                "Hola " + sponsor.getName() + ",\n\n" +
                        "La campana '" + campaign.getTitle() + "' publico una nueva actualizacion:\n\n" +
                        update.getTitle() + "\n\n" +
                        update.getBody()
        ));
    }

    public void sendCampaignApproved(User creator, Campaign campaign) {
        sendEmail(
                creator.getEmail(),
                "Campana aprobada",
                "Hola " + creator.getName() + ",\n\n" +
                        "Tu campana '" + campaign.getTitle() + "' fue aprobada y ya esta activa."
        );
    }

    public void sendCampaignRejected(User creator, Campaign campaign) {
        sendEmail(
                creator.getEmail(),
                "Campana rechazada",
                "Hola " + creator.getName() + ",\n\n" +
                        "Tu campana '" + campaign.getTitle() + "' fue rechazada. Puedes revisarla y volver a enviarla."
        );
    }

    public void sendVerificationEmail(User user, String verificationCode) {
        sendEmail(
                user.getEmail(),
                "Codigo de verificacion Fondea",
                "Hola " + user.getName() + ",\n\n" +
                        "Tu codigo de verificacion es: " + verificationCode
        );
    }

    public void sendNearGoalNotification(User sponsor, Campaign campaign) {
        sendEmail(
                sponsor.getEmail(),
                "Campana cerca de su meta",
                "Hola " + sponsor.getName() + ",\n\n" +
                        "La campana '" + campaign.getTitle() + "' esta cerca de alcanzar su meta."
        );
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Error enviando correo a {}: {}", to, e.getMessage());
        }
    }
}