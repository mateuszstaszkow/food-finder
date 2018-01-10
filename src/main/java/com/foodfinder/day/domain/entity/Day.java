package com.foodfinder.day.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "day")
public class Day implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "date", columnDefinition="TIMESTAMP(6)")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @JoinTable(name = "day_timed_dish",
            joinColumns = @JoinColumn(name = "day_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "timed_dish_id", referencedColumnName = "id")
    )
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<TimedDish> timedDishes;
}