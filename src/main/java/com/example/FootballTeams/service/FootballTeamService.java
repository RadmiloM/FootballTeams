package com.example.FootballTeams.service;

import com.example.FootballTeams.dto.FootballTeamDTO;
import com.example.FootballTeams.entity.FootballTeam;
import com.example.FootballTeams.exception.PlayerIdNotFoundException;
import com.example.FootballTeams.exception.PlayerNameExistsInDatabase;
import com.example.FootballTeams.mapping.FootballTeamMapping;
import com.example.FootballTeams.repository.FootballTeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FootballTeamService {

    private final FootballTeamMapping footballTeamMapping;
    private final FootballTeamRepository footballTeamRepository;

    public FootballTeamService(FootballTeamMapping footballTeamMapping, FootballTeamRepository footballTeamRepository) {
        this.footballTeamMapping = footballTeamMapping;
        this.footballTeamRepository = footballTeamRepository;
    }
    @Cacheable(cacheNames = "footballTeam")
    public List<FootballTeam> findAll(){
        log.info("Fetching data from database");
        return footballTeamRepository.findAll();
    }
    @Cacheable(cacheNames="footballTeam",key = "#id")
    public FootballTeam findById(Integer id){
        log.info("Fetching football team from database");
        Optional<FootballTeam> team = footballTeamRepository.findById(id);
        if(!team.isPresent()){
            log.info("If team is not present");
            throw new PlayerIdNotFoundException("Football team with player id " + id + " is not found in database");
        }
        log.info("If team exists in database");
        return team.get();
    }
    @CacheEvict(cacheNames = "footballTeam",allEntries = true)
    public void createTeam(FootballTeam footballTeam){
        log.info("Checking if team with player name exists in database");
        if(footballTeamRepository.existsByPlayerName(footballTeam.getPlayerName())){
            log.info("Team exists in database");
            throw new PlayerNameExistsInDatabase("Football team with player name " + footballTeam.getPlayerName()
            + " already exists in database");
        }
        log.info("Saving new team");
        footballTeamRepository.save(footballTeam);
    }

    @CacheEvict(cacheNames = "footballTeam",key = "#id",allEntries = true)
    public void deleteById(Integer id){
        log.info("Checking if team exists in database");
        Optional<FootballTeam> team = footballTeamRepository.findById(id);
            if(!team.isPresent()){
                log.info("If team is not present in database");
                throw new PlayerIdNotFoundException("Team with player id " + id +
                        " can not be deleted as player id does not exists in database");
            }
        log.info("Team successfully deleted");
        footballTeamRepository.deleteById(id);
    }
    @CachePut(cacheNames = "footballTeam", key = "#id")
    @CacheEvict(cacheNames = "footballTeam",allEntries = true)
    public void updateTeam(FootballTeamDTO team, Integer id){
        log.info("Checking if team exists in database");
        Optional<FootballTeam> currentTeam = footballTeamRepository.findById(id);
        if(!currentTeam.isPresent()){
            log.info("If team is not present in database");
            throw new PlayerIdNotFoundException("Football team with player id " + id + " does not exists");
        }
        if(footballTeamRepository.existsByPlayerName(team.getPlayerName())){
            throw new PlayerNameExistsInDatabase("Football team with player name " + team.getPlayerName()
                    + " already exists in database");
        }
        log.info("Fetching current team from database");
        FootballTeam teamDB = currentTeam.get();
        log.info("Mapping from footballTeamDTO object to footballTeam");
        FootballTeam updatedTeam = footballTeamMapping.mapToEntity(teamDB,team);
        log.info("Team successfully updated");
        footballTeamRepository.save(updatedTeam);
    }

}
