package com.foodfinder.food.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "composition")
public class Composition implements Serializable {

    @Id
    @GeneratedValue
    private Long nutrient_id;

    @Column(name = "nutrient", length = 100)
    private String nutrient;

    @Column(name = "unit", length = 100)
    private String unit;

    @Column(name = "value")
    private Float value;

    @Column(name = "gm")
    private Float gm;

    public Composition setNutrient(String nutrient) {
        this.nutrient = nutrient;
        return this;
    }
}