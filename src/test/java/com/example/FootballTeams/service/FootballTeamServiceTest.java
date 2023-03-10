package com.example.FootballTeams.service;

import com.example.FootballTeams.dto.FootballTeamDTO;
import com.example.FootballTeams.entity.FootballTeam;
import com.example.FootballTeams.exception.PlayerIdNotFoundException;
import com.example.FootballTeams.exception.PlayerNameExistsInDatabase;
import com.example.FootballTeams.mapping.FootballTeamMapping;
import com.example.FootballTeams.repository.FootballTeamRepository;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FootballTeamServiceTest {
    @Mock
    private FootballTeamRepository footballTeamRepository;
    private FootballTeamService footballTeamTestService;
    @Mock
    private FootballTeamMapping footballTeamMapping;

    @Mock
    private EmailNotificationService emailNotificationService;


    @BeforeEach
    void setUp() throws Exception {
        footballTeamTestService = new FootballTeamService(footballTeamMapping, footballTeamRepository, emailNotificationService);
    }


    @Test
    void testFindListOfAllFootballTeams() {
        //when
        footballTeamTestService.findAll();

        //then
        Mockito.verify(footballTeamRepository).findAll();
    }

    @Test
    void testFindAllFootballTeamPages() {
        // when
        Pageable pageable = PageRequest.of(0, 1);
        footballTeamTestService.findAll(pageable);

        //then
        Mockito.verify(footballTeamRepository).findAll(pageable);
    }

    @Test
    void testFindAllByTeamName() {
        //when
        String team = "Arsenal";
        Pageable pageable = PageRequest.of(0, 1);
        footballTeamTestService.findAllByTeamName(team, pageable);
        //then
        Mockito.verify(footballTeamRepository).findAllByTeam(team, pageable);
    }

    @Test
    void whenFindTeamByIdIsCorrect() {
        // given
        int number = 1;
        FootballTeam footballTeam = new FootballTeam(number, "Arsenal", "Martin Odegard", "MC", 99, 1200000);
        //when
        Mockito.when(footballTeamRepository.findById(number)).thenReturn(Optional.of(footballTeam));
        // then
        footballTeamTestService.findById(number);
        verify(footballTeamRepository).findById(number);
    }


    @Test
    void whenFindTeamByIdIsNotCorrect() {
        // given
        int number = 99;
        Optional<FootballTeam> footballTeam = Optional.empty();
        //when
        given(footballTeamRepository.findById(number))
                .willReturn(footballTeam);
        // then
        assertThrows(PlayerIdNotFoundException.class, () -> {
            footballTeamTestService.findById(number);
        });
    }

    @Test
    void canCreateTeam() {
        //given
        String playerName = "Edgar Davids";
        FootballTeam footballTeam = new FootballTeam(
                22, "Arsenal", playerName, "MC", 99, 1200000
        );
        given(footballTeamRepository.existsByPlayerName(footballTeam.getPlayerName()))
                .willReturn(false);
        // when
        footballTeamTestService.createTeam(footballTeam);
        //then
        ArgumentCaptor<FootballTeam> footballTeamArgumentCaptor =
                ArgumentCaptor.forClass(FootballTeam.class);
        Mockito.verify(footballTeamRepository)
                .save(footballTeamArgumentCaptor.capture());

        FootballTeam capturedFootballTeam = footballTeamArgumentCaptor.getValue();
        assertThat(capturedFootballTeam).isEqualTo(footballTeam);
        doNothing().when(emailNotificationService).sendNewFootballTeamNotification(any(), anyString());
    }

    @Test
    void willThrowWhenPlayerNameIsTaken() {
        //given
        String playerName = "Martin Odegard";
        FootballTeam footballTeam = new FootballTeam(
                22, "Arsenal", playerName, "MC", 99, 1200000
        );
        given(footballTeamRepository.existsByPlayerName(footballTeam.getPlayerName()))
                .willReturn(true);
        // when
        assertThatThrownBy(() -> footballTeamTestService.createTeam(footballTeam))
                .isInstanceOf(PlayerNameExistsInDatabase.class)
                .hasMessageContaining("Football team with player name " + footballTeam.getPlayerName()
                        + " already exists in database");
        //then
        Mockito.verify(footballTeamRepository, Mockito.never()).save(any());
    }

    @Test
    void deleteWhenFootballTeamExistsById() {
        // given
        int number = 1;
        FootballTeam footballTeam = new FootballTeam(number, "Arsenal", "Martin Odegard", "MC", 99, 1200000);
        //when
        Mockito.when(footballTeamRepository.findById(number)).thenReturn(Optional.of(footballTeam));
        footballTeamTestService.deleteById(number);
        // then
        Mockito.verify(footballTeamRepository).deleteById(number);
    }

    @Test
    void deleteWhenFootballTeamPlayerDoesNotExistsById() {
        // given
        int number = 99;
        //when
        Mockito.when(footballTeamRepository.findById(number)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> footballTeamTestService.deleteById(number))
                .isInstanceOf(PlayerIdNotFoundException.class)
                .hasMessageContaining("Team with player id " + number +
                        " can not be deleted as player id does not exists in database");
        // then
        Mockito.verify(footballTeamRepository, Mockito.never()).deleteById(number);
    }

    @Test
    void whenFootballTeamDTOWillUpdateFootballTeamWhichExistsInDatabase() {
        // given
        Integer number = 1;
        FootballTeamDTO footballTeamDTO = new FootballTeamDTO();
        footballTeamDTO.setTeam("Barcelona");
        footballTeamDTO.setPlayerName("Xavi");
        footballTeamDTO.setPosition("MC");
        footballTeamDTO.setSkillLevel(91);
        footballTeamDTO.setPrice(1200000);
        String playerName = "Iniesta";
        FootballTeam footballTeam = new FootballTeam(1, "Barcelona", playerName, "MC", 90, 1200000);
        Mockito.when(footballTeamRepository.findById(number)).thenReturn(Optional.of(footballTeam));
        Mockito.when(footballTeamRepository.existsByPlayerName(playerName)).thenReturn(false);
        Mockito.when(footballTeamMapping.mapToEntity(footballTeam, footballTeamDTO)).thenReturn(footballTeam);
        footballTeamTestService.updateTeam(footballTeamDTO, number);
        Mockito.verify(footballTeamRepository).save(footballTeam);
    }
}