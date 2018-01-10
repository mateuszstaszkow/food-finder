package com.foodfinder.migrator.domain.dto;

import com.foodfinder.food.domain.dto.FoodGroupDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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