package com.foodfinder.user.service;

import com.foodfinder.user.domain.dto.RegistrationDTO;
import com.foodfinder.user.domain.entity.Role;
import com.foodfinder.user.domain.entity.User;
import com.foodfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(RegistrationDTO user) {
        User userEntity = new User();
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        String encryptedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encryptedPassword);
        userEntity.setEnabled(true);
        userEntity.setRole(new Role(2L));

        try {
            userRepository.save(userEntity);
        } catch (Exception exception) {
            throw new BadRequestException(exception);
        }
    }
}
