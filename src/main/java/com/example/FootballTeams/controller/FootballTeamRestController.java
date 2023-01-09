package com.example.FootballTeams.controller;

import com.example.FootballTeams.dto.FootballTeamDTO;
import com.example.FootballTeams.entity.FootballTeam;
import com.example.FootballTeams.mapping.FootballTeamMapping;
import com.example.FootballTeams.page.FootballTeamPageDTO;
import com.example.FootballTeams.service.FootballTeamService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@Data
@Slf4j
public class FootballTeamRestController {

    private final FootballTeamMapping footballTeamMapping;
    private final FootballTeamService footballTeamService;

    @GetMapping("/fetchAllTeams")
    public ResponseEntity<List<FootballTeamDTO>> getAllTeams() {
        List<FootballTeam> footballTeams = footballTeamService.findAll();
        List<FootballTeamDTO> footballTeamDTOS = footballTeamMapping.mapToDTO(footballTeams);
        return ResponseEntity.ok(footballTeamDTOS);
    }

    @GetMapping("/fetchAllPages")
    public ResponseEntity<FootballTeamPageDTO<?>> fetchAllPages(Pageable pageable) {
        Page<FootballTeam> team = footballTeamService.findAll(pageable);
        return ResponseEntity.ok(FootballTeamPageDTO.builder()
                .data(team.getContent())
                .pageNumber(team.getNumber())
                .pageSize(team.getSize())
                .totalPages(team.getTotalPages())
                .build());
    }

    @GetMapping("/fetchPagesWithTeamName")
    public ResponseEntity<FootballTeamPageDTO<?>> findByTeamPages(@RequestParam String team, @PageableDefault Pageable pageable) {
        Page<FootballTeam> footballTeams = footballTeamService.findAllByTeamName(team, pageable);
        return ResponseEntity.ok(FootballTeamPageDTO.builder()
                .data(team)
                .pageNumber(footballTeams.getNumber())
                .pageSize(footballTeams.getSize())
                .totalPages(footballTeams.getTotalPages())
                .build());
    }

    @GetMapping("/findTeamById/{id}")
    public ResponseEntity<FootballTeamDTO> getTeam(@PathVariable Integer id) {
        FootballTeam footballTeam = footballTeamService.findById(id);
        FootballTeamDTO footballTeamDTO = footballTeamMapping.mapToDTO(footballTeam);
        return ResponseEntity.ok(footballTeamDTO);
    }

    @GetMapping("/externalApi")
    public ResponseEntity<Object> getScores() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.openligadb.de/getmatchdata/bl1/2022/8";
        Object response = restTemplate.getForObject(url, Object.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/createTeam")
    public ResponseEntity<Void> createTeam(@Valid @RequestBody FootballTeamDTO footballTeamDTO) {
        FootballTeam footballTeam = footballTeamMapping.mapToEntity(footballTeamDTO);
        footballTeamService.createTeam(footballTeam);
        return ResponseEntity.created(URI.create("/findTeamById/id/" + footballTeam.getId())).build();
    }

    @DeleteMapping("/removeTeam/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Integer id) {
        footballTeamService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateTeam/{id}")
    public ResponseEntity<Void> updateTeam(@Valid @RequestBody FootballTeamDTO footballTeamDTO, @PathVariable Integer id) {
        footballTeamService.updateTeam(footballTeamDTO, id);
        return ResponseEntity.ok().build();
    }

}
