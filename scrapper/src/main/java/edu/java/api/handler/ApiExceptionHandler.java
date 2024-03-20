package edu.java.api.handler;

import edu.java.api.entities.exceptions.BadRequestException;
import edu.java.api.entities.exceptions.NotFoundException;
import edu.java.api.entities.exceptions.RequestProcessingException;
import edu.java.data.response.ApiErrorResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleException(BadRequestException exception) {
        return new ApiErrorResponse(
            exception.getDescription(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            exception.getClass().getSimpleName(),
            exception.getMessage(),
            getListStringStackTrace(exception)
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleException(NotFoundException exception) {
        return new ApiErrorResponse(
            exception.getDescription(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            exception.getClass().getSimpleName(),
            exception.getMessage(),
            getListStringStackTrace(exception)
        );
    }

    @ExceptionHandler(RequestProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleException(RequestProcessingException exception) {
        return new ApiErrorResponse(
            exception.getMessage(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            exception.getClass().getSimpleName(),
            exception.getMessage(),
            getListStringStackTrace(exception)
        );
    }

    private List<String> getListStringStackTrace(Exception exception) {
        List<String> stackTrace = new ArrayList<>();

        for (var stackTraceElement : exception.getStackTrace()) {
            stackTrace.add(stackTraceElement.toString());
        }

        return stackTrace;
    }
}
