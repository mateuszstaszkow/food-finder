package com.foodfinder.migrator.dto;

import lombok.Data;

import java.util.List;

@Data
public class ListDTO {

    private String lt;
    private int start;
    private int end;
    private int total;
    private int sr;
    private String sort;
    private List<FoodGroupResponseDTO> item;
}
