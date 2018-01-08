package com.foodfinder.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private Long id;
    private String name;
    private List<PrivilegeDTO> privileges;

    public RoleDTO(Long id) {
        this.id = id;
    }
}
