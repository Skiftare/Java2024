package edu.java.exceptions.entities;

import edu.java.data.response.ApiErrorResponse;
import lombok.Getter;

@Getter
public class CustomApiException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public CustomApiException(ApiErrorResponse apiErrorResponse) {
        this.errorResponse = apiErrorResponse;
    }
}
