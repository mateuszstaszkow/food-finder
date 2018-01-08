package com.foodfinder.food.repository;

import com.foodfinder.food.domain.entity.FoodGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodGroupRepository extends JpaRepository<FoodGroup, Long> {

    FoodGroup findByName(String description);

    List<FoodGroup> findTop10ByNameContaining(String name);

    List<FoodGroup> findTop10ByTranslatedNameContaining(String name);
}
