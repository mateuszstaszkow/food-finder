package com.foodfinder.food.domain.dto;

import lombok.Data;

@Data
public class GoogleVisionLabelAnnotationDTO {

    private String mid;
    private String description;
    private Float score;
}
