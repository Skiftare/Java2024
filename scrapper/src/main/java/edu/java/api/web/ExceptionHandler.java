package edu.java.api.web;

import edu.java.exceptions.entities.LinkAlreadyExistException;
import edu.java.exceptions.entities.LinkNotFoundException;
import edu.java.exceptions.entities.UserAlreadyExistException;
import edu.java.exceptions.entities.UserNotFoundException;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<String> handleException(UserAlreadyExistException ex) {
        Logger.getAnonymousLogger().info("Handling UserAlreadyExist ex " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(LinkAlreadyExistException.class)
    public ResponseEntity<String> handleException(LinkAlreadyExistException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<String> handleException(LinkNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
    }
}
