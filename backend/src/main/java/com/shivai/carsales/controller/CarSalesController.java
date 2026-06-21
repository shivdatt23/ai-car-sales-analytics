package com.shivai.carsales.controller;


import com.shivai.carsales.commons.response.ApiResponse;
import com.shivai.carsales.dto.MonthlyCountDto;
import com.shivai.carsales.dto.UploadSalesResponse;
import com.shivai.carsales.dto.YearlyCountDto;
import com.shivai.carsales.services.CarSalesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/car-sales")
public class CarSalesController {

    private CarSalesService service;


    public CarSalesController(CarSalesService service) {
        this.service = service;
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<ApiResponse<UploadSalesResponse>> uploadFile(@RequestParam("file") MultipartFile file){

        if(file.isEmpty()){

            UploadSalesResponse response = new UploadSalesResponse(0, 0, 0);


            ApiResponse<UploadSalesResponse> apiResponse = new ApiResponse<>(false, "File is Empty", response, 400, LocalDateTime.now());

            return new ResponseEntity<ApiResponse<UploadSalesResponse>>(apiResponse, HttpStatus.BAD_REQUEST);
        }


        UploadSalesResponse response = service.uploadFile(file);

        ApiResponse<UploadSalesResponse> apiResponse = getApiResponse(response);



        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }










    public ApiResponse<UploadSalesResponse> getApiResponse(UploadSalesResponse response){

        String message=null;
        boolean success=false;


        if(response.getFailedCount()==0){
            message="All records uploaded successfully";
            success=true;
        }
        else if (response.getSuccessCount()==0){
            message="No records uploaded";
            success=false;
        }else{
            message="Uploaded with some errors"+response.getFailedCount()+" rows failed";
            success=false;
        }


        return new ApiResponse<>(success,message,response,HttpStatus.OK.value(),LocalDateTime.now());
    }

    @GetMapping("/yearly-count")
    public ResponseEntity<ApiResponse<List<YearlyCountDto>>> yearlCount()
    {
        List<YearlyCountDto> count = service.getyearlyCarsCount();

        ApiResponse<List<YearlyCountDto>> response = new ApiResponse<>(true, "Data read successfully", count, HttpStatus.OK.value(), LocalDateTime.now());

        return ResponseEntity.ok(response);


    }


@GetMapping("/monthly-count")
    public ResponseEntity<ApiResponse<List<MonthlyCountDto>>> monthLyCountByYear(@RequestParam int year){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Monthly Data Read Successfully",
                service.getMonthlyCountByYear(year),
                HttpStatus.OK.value(),
                LocalDateTime.now()
        ));
    }

}
