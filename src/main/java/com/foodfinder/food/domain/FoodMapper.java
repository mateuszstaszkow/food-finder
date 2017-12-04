package com.foodfinder.food.domain;

import com.foodfinder.migrator.dto.FoodGroupResponseDTO;
import com.foodfinder.food.dto.CompositionDTO;
import com.foodfinder.food.dto.DishDTO;
import com.foodfinder.food.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses=FoodGroupMapper.class)
public interface FoodMapper {

    Composition toEntity(CompositionDTO compositionDTO);

    CompositionDTO toDto(Composition composition);

    Product toEntity(ProductDTO productDTO);

    ProductDTO toDto(Product product);

    Dish toEntity(DishDTO productDTO);

    DishDTO toDto(Dish product);

    List<ProductDTO> productListToDto(List<Product> productList);

    List<DishDTO> dishListToDto(List<Dish> productList);

    @Mappings({
            @Mapping(source = "name", target = "description")
    })
    FoodGroup toEntity(FoodGroupResponseDTO response);
}
