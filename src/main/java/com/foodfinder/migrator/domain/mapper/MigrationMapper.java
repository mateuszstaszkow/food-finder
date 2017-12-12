package com.foodfinder.migrator.domain.mapper;

import com.foodfinder.food.domain.entity.Composition;
import com.foodfinder.food.domain.entity.FoodGroup;
import com.foodfinder.food.domain.entity.Product;
import com.foodfinder.migrator.domain.dto.CompositionResponseDTO;
import com.foodfinder.migrator.domain.dto.FoodGroupResponseDTO;
import com.foodfinder.migrator.domain.dto.ProductResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MigrationMapper {

    FoodGroup toEntity(FoodGroupResponseDTO response);

    @Mappings({
            @Mapping(source = "nutrient_id", target = "id"),
            @Mapping(source = "nutrient", target = "name")
    })
    Composition toEntity(CompositionResponseDTO response);

    @Mappings({
            @Mapping(source = "ndbno", target = "id"),
            @Mapping(source = "nutrients", target = "composition"),
            @Mapping(target = "hits", ignore = true)
    })
    Product toEntity(ProductResponseDTO response);
}
