package com.foodfinder.dish.domain.entity;

import com.foodfinder.food.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "dish")
public class Dish implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, length = 50)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products;

    @Column(name = "hits")
    private Long hits;
}