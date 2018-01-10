package com.foodfinder.migrator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListDTO {

    private String lt;
    private int start;
    private int end;
    private int total;
    private int sr;
    private String sort;
    private List<FoodGroupResponseDTO> item;
}
