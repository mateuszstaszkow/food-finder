package com.foodfinder.migrator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDTO {

    private int sr;
    private List<FoodGroupReportDTO> groups;
    private String subset;
    private int end;
    private int start;
    private int total;
    private List<ProductResponseDTO> foods;
}
