package com.foodfinder.user.domain.dto;

import com.foodfinder.day.domain.dto.DayDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private Boolean enabled;
    private Float weight;
    private Float height;
    private int age;
    private String gender;
    //private List<DietDTO> diets;
    private RoleDTO role;
    private List<DayDTO> days;
}
