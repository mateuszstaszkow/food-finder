package com.foodfinder.food.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {

    private Long ndbno;
    private String name;
    private Float weight;
    private String measure;
    private List<CompositionDTO> nutrients;
    private String description;
    private String shortDescription;
    private FoodGroupDTO foodGroup;
}
