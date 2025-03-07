package Capstone.Aeroponics.utils;


import org.springframework.http.HttpStatus;

import Capstone.Aeroponics.models.response.ErrorResponse;
import Capstone.Aeroponics.models.response.SuccessResponse;

public class ResponseUtils {

    public static <T> SuccessResponse<T> buildSuccessResponse(HttpStatus status, String message) {
        SuccessResponse<T> response = new SuccessResponse<>();
        response.setStatusCode(status.value());
        response.setMessage(message);

        return response;
    }

    public static <T> SuccessResponse<T> buildSuccessResponse(HttpStatus status, String message, T data) {
        SuccessResponse<T> response = new SuccessResponse<>();
        response.setStatusCode(status.value());
        response.setMessage(message);
        response.setData(data);

        return response;
    }

    public static ErrorResponse buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse response = new ErrorResponse();
        response.setStatusCode(status.value());
        response.setMessage(message);

        return response;
    }
}
