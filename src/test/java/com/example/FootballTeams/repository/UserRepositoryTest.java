package com.example.FootballTeams.repository;

import com.example.FootballTeams.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userTestRepository;

    @AfterEach
    void tearDown() {
        userTestRepository.deleteAll();
    }

    @Test
    void itShouldExistsByName() {
        //given
        LocalDateTime localDateTime = LocalDateTime.now();
        String name = "Radmilo";
        User user = new User(11, name,"$2a$12$brJIJr9FwpYJ7G5m3NX3yughvVHUnrFb9ez1nA5MgJ.6OaKi2JIV2","ADMIN",
                localDateTime,localDateTime);
        userTestRepository.save(user);

        //when
        Optional<User> expected = userTestRepository.findByName(name);

        //then
        assertThat(expected).isNotEmpty();
    }

    @Test
    void itShouldNotExistsByName() {
        //given
        String name = "Radmilo";

        //when
        Optional<User> expected = userTestRepository.findByName(name);

        //then
        assertThat(expected).isEmpty();
    }
}