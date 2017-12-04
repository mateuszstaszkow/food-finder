package com.foodfinder.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {

    private Long id;
    private String name;
    private List<UserDTO> users;
    private List<PrivilegeDTO> privileges;
}
