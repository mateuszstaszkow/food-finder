package com.foodfinder.user.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {

    private Long id;
    private String name;
    private List<PrivilegeDTO> privileges;
}
