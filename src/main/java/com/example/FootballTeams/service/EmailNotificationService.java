package com.example.FootballTeams.service;

import com.example.FootballTeams.entity.FootballTeam;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    public EmailNotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendNewFootballTeamNotification(FootballTeam footballTeam, String recipientEmail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("New football team");
        message.setText("team: " + footballTeam.getTeam() + "\n" +
                        "playerName: " + footballTeam.getPlayerName() + "\n" +
                        "position: " + footballTeam.getPosition() + "\n" +
                        "skillLevel: " + footballTeam.getSkillLevel() + "\n" +
                        "price: " + footballTeam.getPrice());
        mailSender.send(message);
    }
}
