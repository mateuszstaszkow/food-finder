package com.foodfinder.food.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompositionDTO {

    private Long id;
    private String name;
    private String unit;
    private Float value;
}
