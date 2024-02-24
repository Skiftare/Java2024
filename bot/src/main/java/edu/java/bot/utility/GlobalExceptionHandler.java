package edu.java.bot.utility;

import java.net.URISyntaxException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(URISyntaxException.class)
    public ResponseEntity<Object> handleURISyntaxException(URISyntaxException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка в URI: " + ex.getMessage());
    }
}

