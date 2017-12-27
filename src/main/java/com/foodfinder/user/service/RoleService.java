package com.foodfinder.user.service;

import com.foodfinder.user.dao.RoleRepository;
import com.foodfinder.user.domain.dto.RoleDTO;
import com.foodfinder.user.domain.entity.Role;
import com.foodfinder.user.domain.mapper.UserMapper;
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
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public List<RoleDTO> getRoleList(Pageable pageable) {
        return Optional.ofNullable(roleRepository.findAll(pageable))
                .map(userMapper::roleListToDto)
                .orElseThrow(NotFoundException::new);
    }

    public RoleDTO getRole(Long id) {
        return Optional.ofNullable(roleRepository.findOne(id))
                .map(userMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    public void postRole(RoleDTO role) {
        Role roleEntity = Optional.ofNullable(role)
                .map(userMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        roleRepository.save(roleEntity);
    }

    public void updateRole(Long id, RoleDTO role) {
        Role roleEntity = Optional.ofNullable(role)
                .map(userMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        roleEntity.setId(id);
        roleRepository.save(roleEntity);
    }
}