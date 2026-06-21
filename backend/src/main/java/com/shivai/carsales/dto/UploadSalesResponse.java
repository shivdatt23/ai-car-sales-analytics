package com.shivai.carsales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadSalesResponse {

    private int totalRecords;
    private int successCount;
    private int failedCount;
}
