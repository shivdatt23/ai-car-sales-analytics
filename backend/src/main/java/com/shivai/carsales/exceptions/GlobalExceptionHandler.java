package com.shivai.carsales.exceptions;

import com.shivai.carsales.commons.response.ApiResponse;
import com.shivai.carsales.dto.UploadSalesResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<UploadSalesResponse>> handleAllExceptions(Exception e){
        UploadSalesResponse response = new UploadSalesResponse(0, 0, 0);

        ApiResponse<UploadSalesResponse> apiResponse = new ApiResponse<>(false, e.getMessage(), response, HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());

        return new ResponseEntity<ApiResponse<UploadSalesResponse>>(apiResponse,HttpStatus.INTERNAL_SERVER_ERROR);


    }















}
