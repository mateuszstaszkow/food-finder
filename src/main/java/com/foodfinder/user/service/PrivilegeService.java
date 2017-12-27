package com.foodfinder.user.service;

import com.foodfinder.user.dao.PrivilegeRepository;
import com.foodfinder.user.domain.dto.PrivilegeDTO;
import com.foodfinder.user.domain.entity.Privilege;
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
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;
    private final UserMapper userMapper;

    public List<PrivilegeDTO> getPrivilegeList(Pageable pageable) {
        return Optional.ofNullable(privilegeRepository.findAll(pageable))
                .map(userMapper::privilegeListToDto)
                .orElseThrow(NotFoundException::new);
    }

    public PrivilegeDTO getPrivilege(Long id) {
        return Optional.ofNullable(privilegeRepository.findOne(id))
                .map(userMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    public void postPrivilege(PrivilegeDTO privilege) {
        Privilege privilegeEntity = Optional.ofNullable(privilege)
                .map(userMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        privilegeRepository.save(privilegeEntity);
    }

    public void updatePrivilege(Long id, PrivilegeDTO privilege) {
        Privilege privilegeEntity = Optional.ofNullable(privilege)
                .map(userMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        privilegeEntity.setId(id);
        privilegeRepository.save(privilegeEntity);
    }
}