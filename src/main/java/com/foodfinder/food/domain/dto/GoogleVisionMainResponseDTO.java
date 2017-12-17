package com.foodfinder.food.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class GoogleVisionMainResponseDTO {

    private List<GoogleVisionResponseDTO> responses;
}
