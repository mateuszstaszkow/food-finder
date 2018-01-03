package com.foodfinder.dish.domain.dto;

import com.foodfinder.food.domain.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishDTO {

    private Long id;
    private String name;
    private String description;
    private List<ProductDTO> products;
    private Long hits;
}
