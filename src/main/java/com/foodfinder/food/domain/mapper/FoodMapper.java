package com.foodfinder.food.domain.mapper;

import com.foodfinder.food.domain.dto.CompositionDTO;
import com.foodfinder.food.domain.dto.DishDTO;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.food.domain.entity.Composition;
import com.foodfinder.food.domain.entity.Dish;
import com.foodfinder.food.domain.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FoodMapper {

    @Mapping(target = "gm", ignore = true)
    Composition toEntity(CompositionDTO compositionDTO);

    CompositionDTO toDto(Composition composition);

    Product toEntity(ProductDTO productDTO);

    ProductDTO toDto(Product product);

    Dish toEntity(DishDTO productDTO);

    DishDTO toDto(Dish product);

    List<ProductDTO> productListToDto(Page<Product> productList);

    List<DishDTO> dishListToDto(List<Dish> productList);
}
