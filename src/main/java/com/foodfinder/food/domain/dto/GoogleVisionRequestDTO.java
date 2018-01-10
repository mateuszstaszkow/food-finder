package com.foodfinder.food.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class GoogleVisionRequestDTO {

    private GoogleVisionImageDTO image;
    private List<GoogleVisionFeatureDTO> features;
}
