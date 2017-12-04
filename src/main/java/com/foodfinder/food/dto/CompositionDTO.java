package com.foodfinder.food.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = CompositionDeserializer.class)
public class CompositionDTO {

    private Long nutrient_id;
    private String nutrient;
    private String unit;
    private Float value; // may appear "--"
    private Float gm; // may appear "--"
}
