package com.example.FootballTeams.controller;

import com.example.FootballTeams.exception.PlayerIdNotFoundException;
import com.example.FootballTeams.exception.PlayerNameExistsInDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PlayerIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleIdNotFoundException(PlayerIdNotFoundException idNotFoundException) {
        ErrorResponse errorResponse = new ErrorResponse(idNotFoundException.getMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlayerNameExistsInDatabase.class)
    public ResponseEntity<ErrorResponse> handlePlayerNameExists(PlayerNameExistsInDatabase playerNameExistsInDatabase) {
        ErrorResponse errorResponse = new ErrorResponse(playerNameExistsInDatabase.getMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse,HttpStatus.BAD_REQUEST);
    }
}
