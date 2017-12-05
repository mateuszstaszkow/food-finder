package com.foodfinder.migrator.domain.dto;

import com.foodfinder.food.domain.dto.FoodGroupDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponseDTO {

    private Long ndbno;
    private String name;
    private Float weight;
    private String measure;
    private List<CompositionResponseDTO> nutrients;
    private String description;
    private String shortDescription;
    private FoodGroupDTO foodGroup;
}