package com.foodfinder.user.domain.mapper;

import com.foodfinder.user.domain.dto.UserDTO;
import com.foodfinder.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDTO userDTO);

    UserDTO toDto(User user);

    List<UserDTO> userListToDto(Page<User> productPage);
}
