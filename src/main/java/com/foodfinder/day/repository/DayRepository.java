package com.foodfinder.day.repository;

import com.foodfinder.day.domain.entity.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {

    Day findById(Long id);
}