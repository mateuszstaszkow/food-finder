package com.foodfinder.diet.domain.mapper;

import com.foodfinder.diet.domain.entity.Diet;
import com.foodfinder.diet.domain.dto.DietDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DietMapper {

    Diet toEntity(DietDTO dietDTO);

    DietDTO toDto(Diet diet);
}
