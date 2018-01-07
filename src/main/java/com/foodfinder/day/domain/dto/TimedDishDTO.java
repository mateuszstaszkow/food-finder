package com.foodfinder.day.domain.dto;

import com.foodfinder.dish.domain.dto.DishDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimedDishDTO {

    private Integer dishOrder;
    private Date date;
    private DishDTO dish;
}
