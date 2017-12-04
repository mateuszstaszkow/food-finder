package com.foodfinder.diet.domain;

import com.foodfinder.diet.dto.DietDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DietMapper {

    Diet toEntity(DietDTO dietDTO);

    DietDTO toDto(Diet diet);
}
