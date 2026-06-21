package com.shivai.carsales.commons.response;

import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private int statusCode;
    private LocalDateTime timestamp;

//    public ApiResponse(boolean success, String message, T data, int statusCode, LocalDateTime timestamp) {
//        this.success = success;
//        this.message = message;
//        this.data = data;
//        this.statusCode = statusCode;
//        this.timestamp = timestamp;
//    }


}
