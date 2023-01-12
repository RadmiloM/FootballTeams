package com.example.FootballTeams.service;

import com.example.FootballTeams.dto.FootballTeamDTO;
import com.example.FootballTeams.entity.FootballTeam;
import com.example.FootballTeams.exception.PlayerIdNotFoundException;
import com.example.FootballTeams.exception.PlayerNameExistsInDatabase;
import com.example.FootballTeams.mapping.FootballTeamMapping;
import com.example.FootballTeams.repository.FootballTeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FootballTeamService {


    @Value("${spring.mail.username}")
    private String recipientEmail;
    private final FootballTeamMapping footballTeamMapping;
    private final FootballTeamRepository footballTeamRepository;

    private final EmailNotificationService emailNotificationService;

    public FootballTeamService(FootballTeamMapping footballTeamMapping, FootballTeamRepository footballTeamRepository, EmailNotificationService emailNotificationService) {
        this.footballTeamMapping = footballTeamMapping;
        this.footballTeamRepository = footballTeamRepository;
        this.emailNotificationService = emailNotificationService;
    }

    @Cacheable(cacheNames = "footballTeam")
    public List<FootballTeam> findAll() {
        log.info("Fetching data from database");
        return footballTeamRepository.findAll();
    }

    public Page<FootballTeam> findAll(Pageable pageable) {
        return footballTeamRepository.findAll(pageable);
    }

    public Page<FootballTeam> findAllByTeamName(String team, Pageable pageable) {
        return footballTeamRepository.findAllByTeam(team, pageable);
    }

    @Cacheable(cacheNames = "footballTeam", key = "#id")
    public FootballTeam findById(Integer id) {
        log.info("Fetching football team from database");
        Optional<FootballTeam> team = footballTeamRepository.findById(id);
        if (!team.isPresent()) {
            log.info("If team is not present");
            throw new PlayerIdNotFoundException("Football team with player id " + id + " is not found in database");
        }
        return team.get();
    }

    @Transactional
    @CacheEvict(cacheNames = "footballTeam", allEntries = true)
    public void createTeam(FootballTeam footballTeam) {
        log.info("Fetching from database");
        if (footballTeamRepository.existsByPlayerName(footballTeam.getPlayerName())) {
            throw new PlayerNameExistsInDatabase("Football team with player name " + footballTeam.getPlayerName()
                    + " already exists in database");
        }
        footballTeamRepository.save(footballTeam);
        emailNotificationService.sendNewFootballTeamNotification(footballTeam, recipientEmail);
    }

    @CacheEvict(cacheNames = "footballTeam", key = "#id", allEntries = true)
    public void deleteById(Integer id) {
        log.info("Checking if team exists in database");
        Optional<FootballTeam> team = footballTeamRepository.findById(id);
        if (!team.isPresent()) {
            log.info("If team is not present in database");
            throw new PlayerIdNotFoundException("Team with player id " + id +
                    " can not be deleted as player id does not exists in database");
        }
        log.info("Team successfully deleted");
        footballTeamRepository.deleteById(id);
    }

    @CachePut(cacheNames = "footballTeam", key = "#id")
    @CacheEvict(cacheNames = "footballTeam", allEntries = true)
    public void updateTeam(FootballTeamDTO team, Integer id) {
        log.info("Checking if team exists in database");
        Optional<FootballTeam> currentTeam = footballTeamRepository.findById(id);
        if (!currentTeam.isPresent()) {
            throw new PlayerIdNotFoundException("Football team with player id " + id + " does not exists");
        }
        if (footballTeamRepository.existsByPlayerName(team.getPlayerName())) {
            throw new PlayerNameExistsInDatabase("Football team with player name " + team.getPlayerName()
                    + " already exists in database");
        }
        FootballTeam teamDB = currentTeam.get();
        log.info("Mapping from footballTeamDTO object to footballTeam");
        FootballTeam updatedTeam = footballTeamMapping.mapToEntity(teamDB, team);
        log.info("Team successfully updated");
        footballTeamRepository.save(updatedTeam);
    }

}
