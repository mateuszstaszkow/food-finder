package com.foodfinder.dish.domain.entity;

import com.foodfinder.food.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "dish")
public class Dish implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    @OneToMany(cascade = CascadeType.MERGE)
    private Set<Product> products;

    @Column(name = "hits")
    private Long hits;

    @Column(name = "name_pl", length = 200)
    private String translatedName;

    @Column(name = "description_pl", length = 2000)
    private String translatedDescription;
}