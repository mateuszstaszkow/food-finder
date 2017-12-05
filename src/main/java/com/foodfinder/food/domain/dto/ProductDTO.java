package com.foodfinder.food.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {

    private Long id;
    private String name;
    private Float weight;
    private String measure;
    private List<CompositionDTO> composition;
    private String description;
    private String shortDescription;
    private FoodGroupDTO foodGroup;
}
