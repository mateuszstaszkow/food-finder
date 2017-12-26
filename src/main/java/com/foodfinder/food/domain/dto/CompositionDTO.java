package com.foodfinder.food.domain.dto;

import lombok.Data;

@Data
public class CompositionDTO {

    private Long id;
    private String name;
    private String unit;
    private Float value;
}
