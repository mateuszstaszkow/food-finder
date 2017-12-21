package com.foodfinder.user.service;

import com.foodfinder.user.dao.UserRepository;
import com.foodfinder.user.domain.entity.User;
import com.foodfinder.user.domain.mapper.UserMapper;
import com.foodfinder.user.domain.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getUserList(Pageable pageable) {
        return Optional.ofNullable(userRepository.findAll(pageable))
                .map(userMapper::userListToDto)
                .orElseThrow(NotFoundException::new);
    }

    public UserDTO getUser(Long id) {
        return Optional.ofNullable(userRepository.findById(id))
                .map(userMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    public void postUser(UserDTO user) {
        User userEntity = Optional.ofNullable(user)
                .map(userMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        userRepository.save(userEntity);
    }

    public void updateUser(Long id, UserDTO user) {
        User userEntity = Optional.ofNullable(user)
                .map(userMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        userEntity.setId(id);
        userRepository.save(userEntity);
    }
}