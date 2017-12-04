package com.foodfinder.user.domain;

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
@Entity(name = "role")
public class Role implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @OneToMany
    private List<User> users;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Privilege> privileges;
}