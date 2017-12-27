package com.foodfinder.dish.repository;

import com.foodfinder.dish.domain.entity.Dish;
import com.foodfinder.food.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findTop10ByNameContaining(String name);

    List<Dish> findTop10ByTranslatedNameContaining(String name);

    @Query("SELECT d FROM dish d WHERE d.name LIKE CONCAT(?1,'%',?2,'%')")
    List<Dish> findByNameStartsWithAndIsCondition(String name, String condition);

    @Query("SELECT d FROM dish d WHERE d.name LIKE CONCAT(?1,'%')")
    List<Dish> findByNameStartsWith(String name);

    @Query("SELECT d FROM dish d WHERE d.translatedName LIKE CONCAT(?1,'%',?2,'%')")
    List<Dish> findByTranslatedNameStartsWithAndIsCondition(String name, String condition);

    @Query("SELECT d FROM dish d WHERE d.translatedName LIKE CONCAT(?1,'%')")
    List<Dish> findByTranslatedNameStartsWith(String name);

    Dish findByProducts(Product product);
}
