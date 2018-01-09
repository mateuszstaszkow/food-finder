package com.foodfinder.user.service;

import com.foodfinder.user.repository.UserRepository;
import com.foodfinder.user.domain.dto.RegistrationDTO;
import com.foodfinder.user.domain.entity.Role;
import com.foodfinder.user.domain.entity.User;
import com.foodfinder.user.domain.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistrationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(RegistrationDTO user) {
        User userEntity = Optional.ofNullable(user)
                .map(userMapper::toEntity)
                .map(this::firstUserConfiguration)
                .orElseThrow(BadRequestException::new);

        try {
            userRepository.save(userEntity);
        } catch (Exception exception) {
            throw new BadRequestException(exception);
        }
    }

    private User firstUserConfiguration(User userEntity) {
        String encryptedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encryptedPassword);
        userEntity.setEnabled(true); //TODO
        userEntity.setRole(new Role(2L));

        return userEntity;
    }
}
