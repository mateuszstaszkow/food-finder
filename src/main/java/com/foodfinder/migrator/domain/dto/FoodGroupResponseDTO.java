package com.foodfinder.migrator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodGroupResponseDTO {

    private int offset;
    private String id;
    private String name;
}
