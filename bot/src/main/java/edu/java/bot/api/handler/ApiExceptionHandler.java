package edu.java.bot.api.handler;

import edu.java.bot.api.entities.exceptions.UpdateAlreadyProcessingExtension;
import edu.java.bot.api.entities.responses.ApiErrorResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UpdateAlreadyProcessingExtension.class)
    public ApiErrorResponse handleException(UpdateAlreadyProcessingExtension ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        String className = stackTrace[0].getClassName();
        String callingMethod = stackTrace[1].getMethodName();

        List<String> stackTraceList = new ArrayList<>();
        stackTraceList.add("Class: " + className);
        stackTraceList.add("Calling method: " + callingMethod);

        return new ApiErrorResponse(
            "Update уже существует",
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getClass().getSimpleName(),
            ex.getMessage(),
            stackTraceList
        );

    }
}
