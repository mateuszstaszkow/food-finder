package com.foodfinder.user.domain.dto;

import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.diet.dto.DietDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private Float weight;
    private Float height;
    private int age;
    private String gender;
    //private List<DietDTO> diets;
    private RoleDTO role;
    private List<DayDTO> days;
}
