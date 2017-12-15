package com.foodfinder.food.domain.entity;

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
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "unit", length = 100)
    private String unit;

    @Column(name = "value")
    private Float value;

    @Column(name = "gm")
    private Float gm;

    @Column(name = "name_pl", length = 200)
    private String translatedName;
}