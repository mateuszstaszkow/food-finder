package com.foodfinder.day.domain.entity;

import com.foodfinder.dish.domain.entity.Dish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "timed_dish")
public class TimedDish implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private Date date;

    @JoinColumn(name = "dish_id")
    private Dish dish;
}