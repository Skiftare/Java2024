package edu.java.exceptions.entities;

import edu.java.data.response.ApiErrorResponse;
import lombok.Getter;

@Getter
public class CustomApiException extends RuntimeException {
    private final ApiErrorResponse errorResponse;
    @Getter private final int statusCode;

    public CustomApiException(ApiErrorResponse apiErrorResponse, int statusCode) {
        this.errorResponse = apiErrorResponse;
        this.statusCode = statusCode;
    }

}
