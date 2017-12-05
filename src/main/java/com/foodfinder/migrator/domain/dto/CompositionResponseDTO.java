package com.foodfinder.migrator.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.foodfinder.migrator.domain.mapper.CompositionDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = CompositionDeserializer.class)
public class CompositionResponseDTO {

    private Long nutrient_id;
    private String nutrient;
    private String unit;
    private Float value; // may appear "--"
    private Float gm; // may appear "--"
}
