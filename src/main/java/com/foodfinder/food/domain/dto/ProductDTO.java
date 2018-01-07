package com.foodfinder.food.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}
