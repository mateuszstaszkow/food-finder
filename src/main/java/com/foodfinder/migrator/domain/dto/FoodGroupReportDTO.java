package com.foodfinder.migrator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodGroupReportDTO {

    private Long id;
    private String description;
}
