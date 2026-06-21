package com.shivai.carsales.services;

import com.shivai.carsales.dto.MonthlyCountDto;
import com.shivai.carsales.dto.UploadSalesResponse;
import com.shivai.carsales.dto.YearlyCountDto;
import com.shivai.carsales.entities.CarSales;
import com.shivai.carsales.repositories.CarSalesRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CarSalesServiceImpl implements CarSalesService {


    private final CarSalesRepository repository;

    public CarSalesServiceImpl(CarSalesRepository repository) {
        this.repository = repository;
    }


    @Override
    public UploadSalesResponse uploadFile(MultipartFile file) {

        int failCount = 0;
        int totalRecords = 0;
        ArrayList<CarSales> cars = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {


            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();

            CSVParser csvParser = CSVParser.parse(bufferedReader, csvFormat);


            for (CSVRecord record : csvParser) {

                try {

                    totalRecords++;
                    String carNumber = record.get("Car Number");
                    Boolean exists = repository.existsByCarNumber(carNumber);

                    if (exists) {
                        failCount++;
                        System.out.println("Duplicate data found " + carNumber);
                        continue;
                    }


                    CarSales carSales = new CarSales();

                    carSales.setCarNumber(record.get("Car Number"));
                    carSales.setBrand(record.get("Brand"));
                    carSales.setModel(record.get("Model"));
                    carSales.setColor(record.get("Color"));

                    carSales.setYear(Integer.parseInt(record.get("Year")));

                    carSales.setDateOfPurchase(
                            LocalDate.parse(
                                    record.get("Date of Purchase"),
                                    java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")
                            )
                    );

                    carSales.setTimeOfPurchase(
                            LocalTime.parse(record.get("Time of Purchase"))
                    );

                    carSales.setPrice(Long.parseLong(record.get("Price (Rs)")));
                    carSales.setMileage(Double.parseDouble(record.get("Mileage (km/l)")));
                    carSales.setEngine(Integer.parseInt(record.get("Engine (cc)")));

                    carSales.setFuelType(record.get("Fuel Type"));
                    carSales.setPaymentMode(record.get("Payment Mode"));
                    carSales.setState(record.get("State"));
                    carSales.setCity(record.get("City"));
                    carSales.setCustomerName(record.get("Customer Name"));
                    carSales.setContactNumber(record.get("Contact Number"));
                    carSales.setEmail(record.get("Email"));

                    carSales.setWarrantyPeriod(
                            Integer.parseInt(record.get("Warranty Period (years)"))
                    );


                    //add the carsales in the list
                    cars.add(carSales);

                }catch (Exception e){
                    failCount++;
                    throw new Exception("Failed to process row :"+record.getRecordNumber());
                }


            }


            if (!cars.isEmpty()) {
                repository.saveAll(cars);
            }


        } catch (Exception e) {

            throw new RuntimeException("Unable to parse CSV " + e.getMessage());

        }

        int successCount = totalRecords - failCount;


        return new UploadSalesResponse(totalRecords, successCount, failCount);
    }

    @Override
    public List<YearlyCountDto> getyearlyCarsCount() {
        return repository.getYearlyCount();
    }

    @Override
    public List<MonthlyCountDto> getMonthlyCountByYear(int year) {
        List<MonthlyCountDto> data = repository.getMonthlyCountByYear(year);

        Map<Integer, Long> map = data.stream().collect(Collectors.toMap(
                MonthlyCountDto::month,
                MonthlyCountDto::count
        ));

        ArrayList<MonthlyCountDto> result = new ArrayList<>();

        for(int i=1;i<=12;i++){
            result.add(new MonthlyCountDto(i,map.getOrDefault(i,0l)));
        }
        return result;
    }


}
