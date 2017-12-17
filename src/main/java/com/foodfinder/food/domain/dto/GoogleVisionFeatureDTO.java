package com.foodfinder.food.domain.dto;

import lombok.Data;

@Data
public class GoogleVisionFeatureDTO {

    private String type;
    private int maxResults;
}
