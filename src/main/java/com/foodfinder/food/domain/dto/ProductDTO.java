package com.foodfinder.food.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {

    private Long id;
    private String name;
    private Float weight;
    private String measure;
    private CompositionDTO protein;
    private CompositionDTO carbohydrates;
    private CompositionDTO fat;
    private CompositionDTO energy;
    private List<CompositionDTO> composition;
    private FoodGroupDTO foodGroup;
    private Long hits;
}
