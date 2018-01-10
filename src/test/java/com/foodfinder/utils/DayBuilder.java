package com.foodfinder.utils;

import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.domain.dto.TimedDishDTO;
import com.foodfinder.day.domain.entity.Day;
import com.foodfinder.day.domain.entity.TimedDish;
import com.foodfinder.dish.domain.dto.DishDTO;
import com.foodfinder.dish.domain.entity.Dish;
import com.foodfinder.food.domain.dto.ProductDTO;

import java.util.*;

public class DayBuilder {

    public static DayDTO getDayDTO(List<ProductDTO> products, Long dayId, Long dishId) {
        DishDTO dishDTO = new DishDTO(dishId, "test dish", "very good", products);
        TimedDishDTO timedDishDTO = new TimedDishDTO(1, new Date(0), dishDTO);
        List<TimedDishDTO> timedDishes = Collections.singletonList(timedDishDTO);

        return DayDTO.builder()
                .id(dayId)
                .date(new Date(0))
                .timedDishes(timedDishes)
                .build();
    }

    public static Day getDayEntity() {
        Dish dish = Dish.builder()
                .id(1L)
                .name("test dish")
                .description("very good")
                .products(new HashSet<>())
                .hits(10L)
                .translatedName("Testowe danie")
                .translatedDescription("Bardzo dobre")
                .build();
        TimedDish timedDish = new TimedDish(1L,1, new Date(0), dish);
        Set<TimedDish> timedDishes = Collections.singleton(timedDish);

        return Day.builder()
                .id(1L)
                .name("Monday")
                .date(new Date(0))
                .timedDishes(timedDishes)
                .build();
    }

}
