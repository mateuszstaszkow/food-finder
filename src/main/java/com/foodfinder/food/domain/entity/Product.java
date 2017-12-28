package com.foodfinder.food.domain.entity;

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
@Entity(name = "product")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = 200)
    private String name;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "measure", length = 100)
    private String measure;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Composition> composition;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "short_description", length = 100)
    private String shortDescription;

    @ManyToOne(cascade = CascadeType.MERGE)
    private FoodGroup foodGroup;

    @Column(name = "hits")
    private Long hits;

    @Column(name = "name_pl", length = 200)
    private String translatedName;

    public Product setFoodGroup(FoodGroup foodGroup) {
        this.foodGroup = foodGroup;
        return this;
    }
}