package com.example.FootballTeams.controller;

import com.example.FootballTeams.dto.FootballTeamDTO;
import com.example.FootballTeams.entity.FootballTeam;
import com.example.FootballTeams.mapping.FootballTeamMapping;
import com.example.FootballTeams.service.FootballTeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FootballTeamController {

    private final FootballTeamService footballTeamService;

    private final FootballTeamMapping footballTeamMapping;

    public FootballTeamController(FootballTeamService footballTeamService, FootballTeamMapping footballTeamMapping) {
        this.footballTeamService = footballTeamService;
        this.footballTeamMapping = footballTeamMapping;
    }

    @GetMapping("/footballController")
    public String getPage(Model model) {
        List<FootballTeam> teamDB = footballTeamService.findAll();
        List<FootballTeamDTO> teamDTO = footballTeamMapping.mapToDTO(teamDB);
        model.addAttribute("teamDTO", teamDTO);
        return "football_team";
    }
}
