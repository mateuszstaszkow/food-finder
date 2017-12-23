package com.foodfinder.user.domain.entity;

import com.foodfinder.diet.domain.Diet;
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
@Entity(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "surname", length = 50)
    private String surname;

    @Column(name = "email", unique = true, nullable = false, length = 50)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    //@ManyToMany(cascade = CascadeType.ALL)
    //private List<Diet> diets;

    @ManyToOne(cascade = CascadeType.ALL)
    private Role role;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "height")
    private Float height;

    @Column(name = "age")
    private int age;

    @Column(name = "gender", length = 50)
    private String gender;
}