package com.shivai.carsales.services;

import com.shivai.carsales.dto.MonthlyCountDto;
import com.shivai.carsales.dto.UploadSalesResponse;
import com.shivai.carsales.dto.YearlyCountDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CarSalesService {

    UploadSalesResponse uploadFile(MultipartFile file);

    List<YearlyCountDto> getyearlyCarsCount();

    List<MonthlyCountDto> getMonthlyCountByYear(int year);


}
