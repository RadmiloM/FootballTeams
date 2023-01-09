package com.example.FootballTeams.exception;

public class UsernameNotFoundException extends RuntimeException {

    public UsernameNotFoundException(String message){
        super(message);
    }
}
