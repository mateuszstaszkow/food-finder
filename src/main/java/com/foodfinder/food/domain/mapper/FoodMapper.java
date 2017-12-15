package com.foodfinder.food.domain.mapper;

import com.foodfinder.food.domain.dto.CompositionDTO;
import com.foodfinder.food.domain.dto.FoodGroupDTO;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.food.domain.entity.Composition;
import com.foodfinder.food.domain.entity.FoodGroup;
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

    List<ProductDTO> productListToDto(Page<Product> productPage);

    List<ProductDTO> productListToDto(List<Product> productList);

    FoodGroupDTO toDto(FoodGroup foodGroup);

    FoodGroup toEntity(FoodGroupDTO foodGroupDTO);

    List<FoodGroupDTO> foodGroupListToDto(Page<FoodGroup> foodGroupPage);

    List<FoodGroupDTO> foodGroupListToDto(List<FoodGroup> foodGroupList);

    List<CompositionDTO> compositionListToDto(Page<Composition> compositionPage);

    List<CompositionDTO> compositionListToDto(List<Composition> compositionList);
}
