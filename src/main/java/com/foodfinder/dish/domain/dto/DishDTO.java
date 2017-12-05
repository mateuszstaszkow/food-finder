package com.foodfinder.dish.domain.dto;

import com.foodfinder.food.domain.dto.ProductDTO;
import lombok.Data;

import java.util.List;

@Data
public class DishDTO {

    private Long id;
    private String name;
    private String description;
    private List<ProductDTO> products;
}
