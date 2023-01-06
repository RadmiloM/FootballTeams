package com.example.FootballTeams.service;

import com.example.FootballTeams.dto.FootballTeamDTO;
import com.example.FootballTeams.entity.FootballTeam;
import com.example.FootballTeams.exception.PlayerIdNotFoundException;
import com.example.FootballTeams.exception.PlayerNameExistsInDatabase;
import com.example.FootballTeams.mapping.FootballTeamMapping;
import com.example.FootballTeams.repository.FootballTeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FootballTeamService {

    private final FootballTeamMapping footballTeamMapping;
    private final FootballTeamRepository footballTeamRepository;

    public FootballTeamService(FootballTeamMapping footballTeamMapping, FootballTeamRepository footballTeamRepository) {
        this.footballTeamMapping = footballTeamMapping;
        this.footballTeamRepository = footballTeamRepository;
    }

    public List<FootballTeam> findAll(){
        return footballTeamRepository.findAll();
    }

    public FootballTeam findById(Integer id){
        Optional<FootballTeam> team = footballTeamRepository.findById(id);
        if(!team.isPresent()){
            throw new PlayerIdNotFoundException("Football team with player id " + id + " is not found in database");
        }
        return team.get();
    }

    public void createTeam(FootballTeamDTO footballTeamDTO){
        if(footballTeamRepository.existsByPlayerName(footballTeamDTO.getPlayerName())){
            throw new PlayerNameExistsInDatabase("Football team with player name " + footballTeamDTO.getPlayerName()
            + " already exists in database");
        }
        FootballTeam savedFootballTeam = footballTeamMapping.mapToEntity(footballTeamDTO);
        footballTeamRepository.save(savedFootballTeam);
    }

    public void deleteById(Integer id){
        Optional<FootballTeam> team = footballTeamRepository.findById(id);
            if(!team.isPresent()){
                throw new PlayerIdNotFoundException("team with player id " + id +
                        " can not be deleted as player id does not exists in database");
            }
        footballTeamRepository.deleteById(id);
    }

    public void updateTeam(FootballTeamDTO team, Integer id){
        Optional<FootballTeam> currentTeam = footballTeamRepository.findById(id);
        if(!currentTeam.isPresent()){
            throw new PlayerIdNotFoundException("Football team with player id " + id + " does not exists");
        }
        FootballTeam teamDB = currentTeam.get();
        FootballTeam updatedTeam = footballTeamMapping.mapToEntity(teamDB,team);
        footballTeamRepository.save(updatedTeam);
    }

}
