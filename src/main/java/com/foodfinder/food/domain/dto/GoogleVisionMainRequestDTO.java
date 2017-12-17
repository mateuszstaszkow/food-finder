package com.foodfinder.food.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class GoogleVisionMainRequestDTO {

    private List<GoogleVisionRequestDTO> requests;
}
