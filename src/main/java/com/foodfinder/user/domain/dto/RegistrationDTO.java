package com.foodfinder.user.domain.dto;

import lombok.Data;

@Data
public class RegistrationDTO {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Boolean enabled;
    private RoleDTO role;
    private Float weight;
    private Float height;
    private int age;
    private String gender;
}