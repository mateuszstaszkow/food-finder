package com.foodfinder.food.dao;

import com.foodfinder.food.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findById(Long id);

    Product findByName(String name);

    List<Product> findTop10ByNameContaining(String name);
}