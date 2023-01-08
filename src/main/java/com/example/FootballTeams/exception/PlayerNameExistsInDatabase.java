package com.example.FootballTeams.exception;

public class PlayerNameExistsInDatabase extends RuntimeException {

    public PlayerNameExistsInDatabase(String message) {
        super(message);
    }
}
