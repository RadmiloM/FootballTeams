package com.example.FootballTeams.repository;

import com.example.FootballTeams.entity.FootballTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FootballTeamRepository extends JpaRepository<FootballTeam, Integer> {

    Boolean existsByPlayerName(String name);

    Page<FootballTeam> findAllByTeam(String team, Pageable pageable);


}
