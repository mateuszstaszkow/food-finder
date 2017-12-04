package com.foodfinder.user.domain;

import com.foodfinder.user.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDTO userDTO);

    UserDTO toDto(User user);
}
