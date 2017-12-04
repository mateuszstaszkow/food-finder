package com.foodfinder.diet.dto;

import com.foodfinder.user.dto.UserDTO;
import lombok.Data;

import java.util.List;

@Data
public class DietDTO {

    private Long id;
    private String name;
    private List<UserDTO> users;
}
