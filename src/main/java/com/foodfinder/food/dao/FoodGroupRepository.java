package com.foodfinder.food.dao;

import com.foodfinder.food.domain.entity.FoodGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodGroupRepository extends JpaRepository<FoodGroup, Long> {

    FoodGroup findByName(String description);
}
