package com.example.FootballTeams.controller;

import com.example.FootballTeams.dto.FootballTeamDTO;
import com.example.FootballTeams.entity.FootballTeam;
import com.example.FootballTeams.service.FootballTeamService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;

@RestController
@Data
public class FootballTeamController {

    private final FootballTeamService footballTeamService;

    @GetMapping("/fetchAllTeams")
    public List<FootballTeam> getAllTeams() {
        return footballTeamService.findAll();
    }

    @GetMapping("/findTeamById/{id}")
    public FootballTeam getTeam(@PathVariable Integer id) {
        return footballTeamService.findById(id);
    }

    @GetMapping("/externalApi")
    public Object getScores() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.openligadb.de/getmatchdata/bl1/2022/8";
        Object response = restTemplate.getForObject(url, Object.class);
        return response;
    }

    @PostMapping("/createTeam")
    public void createTeam(@Valid @RequestBody FootballTeamDTO footballTeamDTO) {
        footballTeamService.createTeam(footballTeamDTO);
    }

    @DeleteMapping("/removeTeam/{id}")
    public void removeTeam(@PathVariable Integer id) {
        footballTeamService.deleteById(id);
    }

    @PutMapping("/updateTeam/{id}")
    public void updateTeam(@Valid @RequestBody FootballTeamDTO footballTeamDTO, @PathVariable Integer id) {
        footballTeamService.updateTeam(footballTeamDTO, id);

    }

}
