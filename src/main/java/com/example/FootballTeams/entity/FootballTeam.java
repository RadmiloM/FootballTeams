package com.example.FootballTeams.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "football_teams")
@AllArgsConstructor
@NoArgsConstructor
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
