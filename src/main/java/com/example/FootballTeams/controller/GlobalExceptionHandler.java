package com.example.FootballTeams.controller;

import com.example.FootballTeams.error.ErrorResponse;
import com.example.FootballTeams.exception.PlayerIdNotFoundException;
import com.example.FootballTeams.exception.PlayerNameExistsInDatabase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exceptions,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        HashMap<String,String> footballTeamErrors = new HashMap<>();
        exceptions.getBindingResult().getAllErrors().forEach((error)-> {
            String footballTeamFieldError = ((FieldError) error).getField();
            String footballTeamMessage = error.getDefaultMessage();
            footballTeamErrors.put(footballTeamFieldError,footballTeamMessage);
        });
        return new ResponseEntity<Object>(footballTeamErrors,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(PlayerIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleIdNotFoundException(PlayerIdNotFoundException idNotFoundException) {
        ErrorResponse errorResponse = new ErrorResponse(idNotFoundException.getMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlayerNameExistsInDatabase.class)
    public ResponseEntity<ErrorResponse> handlePlayerNameExists(PlayerNameExistsInDatabase playerNameExistsInDatabase) {
        ErrorResponse errorResponse = new ErrorResponse(playerNameExistsInDatabase.getMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
