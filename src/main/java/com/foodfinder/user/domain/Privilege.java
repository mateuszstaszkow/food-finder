package com.foodfinder.user.domain;

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
@Entity(name = "privilege")
public class Privilege implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", length = 50)
    private String name;
}