package com.foodfinder.day.repository;

import com.foodfinder.day.domain.entity.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {

    @Query(value = "SELECT * FROM day d WHERE d.date = ?1", nativeQuery = true)
    List<Day> findByDateString(String date);

    List<Day> findByDateBetween(Date from, Date to);
}