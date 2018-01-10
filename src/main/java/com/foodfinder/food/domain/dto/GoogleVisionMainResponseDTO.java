package com.foodfinder.food.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleVisionMainResponseDTO {

    private List<GoogleVisionResponseDTO> responses;
}
