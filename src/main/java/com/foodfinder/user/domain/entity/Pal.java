package com.foodfinder.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "pal")
public class Pal {

    @Id
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "value", nullable = false)
    private Float value;
}
