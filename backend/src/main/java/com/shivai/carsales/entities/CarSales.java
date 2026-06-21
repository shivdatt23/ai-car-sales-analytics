package com.shivai.carsales.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "car_sales")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "car_number",unique = true)
    private String carNumber;

    private String brand;
    private String model;
    private String color;

    private int year;

    @Column(name = "date_of_purchase")
    private LocalDate dateOfPurchase;

    @Column(name = "time_of_purchase")
    private LocalTime timeOfPurchase;
    private long price;
    private double mileage;
    private  int engine;
    private String fuelType;

    @Column(name = "payment_mode")
    private String paymentMode;
    private String state;
    private String city;
    private String customerName;

    @Column(name = "contact_number")
    private String contactNumber;

    private String email;

    @Column(name = "warranty_period")
    private int warrantyPeriod;






}
