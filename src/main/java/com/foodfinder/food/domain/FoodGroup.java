package com.foodfinder.food.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "food_group")
public class FoodGroup implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "description", unique = true, nullable = false, length = 100)
    private String description;

    public FoodGroup setDescription(String description) {
        this.description = description;
        return this;
    }
}
