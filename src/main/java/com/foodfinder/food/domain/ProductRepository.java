package com.foodfinder.food.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByNdbno(Long ndbno);

    Product findByName(String name);
}