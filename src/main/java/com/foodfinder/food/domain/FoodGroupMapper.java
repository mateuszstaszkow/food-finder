package com.foodfinder.food.domain;

import org.springframework.stereotype.Component;

@Component
class FoodGroupMapper {

    FoodGroup map(String value) {
        FoodGroup foodGroup = new FoodGroup();
        foodGroup.setDescription(value);
        return foodGroup;
    }
}
