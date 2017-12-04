package com.foodfinder.user.domain;

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

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "users")
    private List<Diet> diets;

    @OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="id", nullable = false)
    private Role role;
}