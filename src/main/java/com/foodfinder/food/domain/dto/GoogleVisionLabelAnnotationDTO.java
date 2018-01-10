package com.foodfinder.food.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleVisionLabelAnnotationDTO {

    private String mid;
    private String description;
    private Float score;
}
