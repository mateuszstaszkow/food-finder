package com.foodfinder.migrator.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReportDTO {

    private int sr;
    private List<FoodGroupReportDTO> groups;
    private String subset;
    private int end;
    private int start;
    private int total;
    private List<ProductResponseDTO> foods;
}
