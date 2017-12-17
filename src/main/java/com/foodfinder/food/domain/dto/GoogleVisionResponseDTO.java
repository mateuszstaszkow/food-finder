package com.foodfinder.food.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class GoogleVisionResponseDTO {

    private List<GoogleVisionLabelAnnotationDTO> labelAnnotations;
}
