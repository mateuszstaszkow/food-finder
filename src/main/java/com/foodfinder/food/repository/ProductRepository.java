package com.foodfinder.food.repository;

import com.foodfinder.food.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findById(Long id);

    Product findByName(String name);

    List<Product> findTop10ByNameContaining(String name);

    List<Product> findTop10ByTranslatedNameContaining(String name);

    @Query("SELECT p FROM product p WHERE p.name LIKE CONCAT(?1,'%',?2,'%')")
    List<Product> findByNameStartsWithAndIsCondition(String name, String condition);

    @Query("SELECT p FROM product p WHERE p.name LIKE CONCAT(?1,'%')")
    List<Product> findByNameStartsWith(String name);

    @Query("SELECT p FROM product p WHERE p.translatedName LIKE CONCAT(?1,'%',?2,'%')")
    List<Product> findByTranslatedNameStartsWithAndIsCondition(String name, String condition);

    @Query("SELECT p FROM product p WHERE p.translatedName LIKE CONCAT(?1,'%')")
    List<Product> findByTranslatedNameStartsWith(String name);

    List<Product> findByIdIn(List<Long> id);
}