package com.foodfinder.day.domain.dto;

import com.foodfinder.dish.domain.dto.DishDTO;
import lombok.Data;

import java.util.Date;

@Data
public class TimedDishDTO {

    private Long id;
    private Date date;
    private DishDTO dish;
}
