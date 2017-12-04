package com.foodfinder.diet.domain;

import com.foodfinder.user.domain.User;
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
@Entity(name = "diet")
public class Diet implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    private List<User> users;

}