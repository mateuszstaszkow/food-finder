package com.foodfinder.migrator.dto;

import com.foodfinder.food.dto.FoodGroupDTO;
import com.foodfinder.food.dto.ProductDTO;
import lombok.Data;

import java.util.List;

@Data
public class ReportDTO {

    private int sr;
    private List<FoodGroupDTO> groups;
    private String subset;
    private int end;
    private int start;
    private int total;
    private List<ProductDTO> foods;
}
