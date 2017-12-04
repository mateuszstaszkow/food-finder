package com.foodfinder.food.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodGroupRepository extends JpaRepository<FoodGroup, Long> {

    FoodGroup findByDescription(String description);
}
