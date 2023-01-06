package com.example.FootballTeams.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "football_teams")
public class FootballTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String team;

    private String playerName;

    private String position;

    private Integer skillLevel;

    private Integer price;
}
