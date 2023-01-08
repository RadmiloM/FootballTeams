package com.example.FootballTeams.mapping;

import com.example.FootballTeams.dto.FootballTeamDTO;
import com.example.FootballTeams.entity.FootballTeam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FootballTeamMapping {

    public FootballTeamDTO mapToDTO(FootballTeam footballTeam){
        FootballTeamDTO footballTeamDTO = new FootballTeamDTO();
        footballTeamDTO.setTeam(footballTeam.getTeam());
        footballTeamDTO.setPlayerName(footballTeam.getPlayerName());
        footballTeamDTO.setPosition(footballTeam.getPosition());
        footballTeamDTO.setSkillLevel(footballTeam.getSkillLevel());
        footballTeamDTO.setPrice(footballTeam.getPrice());
        return footballTeamDTO;
    }

    public FootballTeam mapToEntity(FootballTeamDTO footballTeamDTO){
        FootballTeam footballTeam = new FootballTeam();
        footballTeam.setTeam(footballTeamDTO.getTeam());
        footballTeam.setPlayerName(footballTeamDTO.getPlayerName());
        footballTeam.setPosition(footballTeamDTO.getPosition());
        footballTeam.setSkillLevel(footballTeamDTO.getSkillLevel());
        footballTeam.setPrice(footballTeamDTO.getPrice());
        return footballTeam;
    }

    public FootballTeam mapToEntity(FootballTeam footballTeam,FootballTeamDTO footballTeamDTO){
        footballTeam.setTeam(footballTeamDTO.getTeam());
        footballTeam.setPlayerName(footballTeamDTO.getPlayerName());
        footballTeam.setPosition(footballTeamDTO.getPosition());
        footballTeam.setSkillLevel(footballTeamDTO.getSkillLevel());
        footballTeam.setPrice(footballTeamDTO.getPrice());
        return  footballTeam;

    }

    public List<FootballTeamDTO> mapToDTO(List<FootballTeam> footballTeams){
        return  footballTeams.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
}
