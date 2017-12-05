package com.foodfinder.dish.domain.mapper;

import com.foodfinder.dish.domain.dto.DishDTO;
import com.foodfinder.dish.domain.entity.Dish;
import com.foodfinder.food.domain.mapper.FoodMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = FoodMapper.class)
public interface DishMapper {

    Dish toEntity(DishDTO productDTO);

    DishDTO toDto(Dish product);

    List<DishDTO> dishListToDto(List<Dish> productList);
}
