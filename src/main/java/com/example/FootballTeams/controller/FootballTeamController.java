package com.example.FootballTeams.controller;

import com.example.FootballTeams.entity.FootballTeam;
import com.example.FootballTeams.service.FootballTeamService;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Data
public class FootballTeamController {

    private final FootballTeamService footballTeamService;

    @GetMapping("/fetchAllTeams")
    public List<FootballTeam> getAllTeams(){
     return footballTeamService.findAll();
    }
}
