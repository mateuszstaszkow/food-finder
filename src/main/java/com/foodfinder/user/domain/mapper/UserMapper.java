package com.foodfinder.user.domain.mapper;

import com.foodfinder.day.domain.mapper.DayMapper;
import com.foodfinder.user.domain.dto.PrivilegeDTO;
import com.foodfinder.user.domain.dto.RegistrationDTO;
import com.foodfinder.user.domain.dto.RoleDTO;
import com.foodfinder.user.domain.dto.UserDTO;
import com.foodfinder.user.domain.entity.Privilege;
import com.foodfinder.user.domain.entity.Role;
import com.foodfinder.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", uses = DayMapper.class)
public interface UserMapper {

    @Mappings({
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    User toEntity(UserDTO userDTO);

    UserDTO toDto(User user);

    List<UserDTO> userListToDto(Page<User> userPage);

    Role toEntity(RoleDTO userDTO);

    RoleDTO toDto(Role user);

    Privilege toEntity(PrivilegeDTO userDTO);

    PrivilegeDTO toDto(Privilege user);

    List<RoleDTO> roleListToDto(Page<Role> rolePage);

    List<PrivilegeDTO> privilegeListToDto(Page<Privilege> privilegePage);

    @Mapping(target = "days", ignore = true)
    User toEntity(RegistrationDTO userDTO);
}
