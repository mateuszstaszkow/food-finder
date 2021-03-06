package com.foodfinder.day.repository;

import com.foodfinder.day.domain.entity.TimedDish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimedDishRepository extends JpaRepository<TimedDish, Long> {

}
