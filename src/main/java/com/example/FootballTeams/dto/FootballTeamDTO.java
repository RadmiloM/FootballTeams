package com.example.FootballTeams.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class FootballTeamDTO {

    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "^[A-Z][a-zA-Z\\s]*$", message = "team must contain only letters and first letter must be uppercase")
    private String team;
    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "^[A-Z][a-zA-Z\\s]*$", message = "player name must contain only letters and first letter must be uppercase")
    private String playerName;
    @NotBlank
    @Size(max = 8)
    @Pattern(regexp = "^[A-Z][a-zA-Z\\s]*$", message = "position must contain only letters and first letter must be uppercase")
    private String position;
    @NotNull
    private Integer skillLevel;
    @NotNull
    private Integer price;
}
