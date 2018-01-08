package com.foodfinder.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationDTO {

    private String name;
    private String surname;
    private String email;
    private String password;
    private Float weight;
    private Float height;
    private int age;
    private String gender;
}
