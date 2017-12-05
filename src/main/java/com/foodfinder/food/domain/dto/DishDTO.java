package com.foodfinder.food.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class DishDTO {

    private Long id;
    private String name;
    private String description;
    private List<ProductDTO> products;
}
