package com.foodfinder.user.dto;

import com.foodfinder.diet.dto.DietDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private List<DietDTO> diets;
    private RoleDTO role;
}
