package com.example.FootballTeams.service;

import com.example.FootballTeams.entity.FootballTeam;
import com.example.FootballTeams.repository.FootballTeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FootballTeamService {

    private final FootballTeamRepository footballTeamRepository;

    public FootballTeamService(FootballTeamRepository footballTeamRepository) {
        this.footballTeamRepository = footballTeamRepository;
    }


    public List<FootballTeam> findAll(){
        return footballTeamRepository.findAll();
    }
}
