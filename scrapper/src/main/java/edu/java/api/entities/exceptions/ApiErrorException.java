package edu.java.api.entities.exceptions;

import edu.java.api.entities.responses.ApiErrorResponse;
import lombok.Getter;

@Getter
public class ApiErrorException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public ApiErrorException(ApiErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}
