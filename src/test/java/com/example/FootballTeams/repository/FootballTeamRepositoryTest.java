package com.example.FootballTeams.repository;

import com.example.FootballTeams.entity.FootballTeam;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FootballTeamRepositoryTest {
    @Autowired
    private FootballTeamRepository footballTeamTest;

    @AfterEach
    void tearDown() {
        footballTeamTest.deleteAll();
    }

    @Test
    void itShouldCheckIfFootballTeamPlayerExistsByPlayerName() {
        // given
        String playerName = "Martin Odegard";
        FootballTeam footballTeam = new FootballTeam(
                22, "Arsenal", playerName, "MC", 99, 1200000
        );
        footballTeamTest.save(footballTeam);

        // when

        Boolean expected = footballTeamTest.existsByPlayerName(playerName);

        // then
        assertThat(expected).isTrue();

    }


    @Test
    void itShouldCheckIfFootballTeamPlayerNameDoesNotExists() {
        // given
        String playerName = "Martin Odegard";

        // when

        Boolean expected = footballTeamTest.existsByPlayerName(playerName);

        // then
        assertThat(expected).isFalse();

    }

    @Test
    void itShouldCheckIfGivenTeamExists() {
        // given
        String team = "Arsenal";
        FootballTeam footballTeam = new FootballTeam(1, team, "Martin Odegard", "MC", 99, 1200000);
        Pageable pageable = PageRequest.of(0, 1);
        footballTeamTest.save(footballTeam);

        // when
        Page<FootballTeam> expected = footballTeamTest.findAllByTeam(team, pageable);

        //then
        assertThat(expected).isNotEmpty();

    }

    @Test
    void itShouldCheckIfGivenTeamDoesNotExists() {
        // given
        String team = "Arsenal";
        FootballTeam footballTeam = new FootballTeam(1, team, "Martin Odegard", "MC", 99, 1200000);
        Pageable pageable = PageRequest.of(0, 1);

        // when
        Page<FootballTeam> expected = footballTeamTest.findAllByTeam(team, pageable);

        //then
        assertThat(expected).isEmpty();

    }
}