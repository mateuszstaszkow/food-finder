package com.foodfinder.utils;

import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.domain.dto.TimedDishDTO;
import com.foodfinder.dish.domain.dto.DishDTO;
import com.foodfinder.food.domain.dto.ProductDTO;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DayBuilder {

    public static DayDTO getDay(List<ProductDTO> products, Long dayId, Long dishId) {
        DishDTO dishDTO = new DishDTO(dishId, "test dish", "very good", products);
        TimedDishDTO timedDishDTO = new TimedDishDTO(1, new Date(0), dishDTO);
        List<TimedDishDTO> timedDishes = Collections.singletonList(timedDishDTO);

        return DayDTO.builder()
                .id(dayId)
                .name("Monday")
                .date(new Date(0))
                .timedDishes(timedDishes)
                .build();
    }

}
