package com.foodfinder.user;

import com.foodfinder.user.domain.dto.RoleDTO;
import com.foodfinder.user.domain.entity.Role;
import com.foodfinder.user.domain.mapper.UserMapper;
import com.foodfinder.user.repository.RoleRepository;
import com.foodfinder.user.service.RoleService;
import com.foodfinder.utils.RestControllerTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class RoleServiceTest {

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserMapper roleMapper;

    private RoleService roleService;
    private PageRequest defaultPageRequest;
    private Role role;
    private RoleDTO roleDTO;
    private List<RoleDTO> rolesDTO;
    private Page<Role> rolesPage;

    @Before
    public void setup() {
        roleService = new RoleService(roleRepository, roleMapper);
        defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        role = new Role(1L, "TEST", new HashSet<>());
        rolesPage = new PageImpl<>(Collections.singletonList(role));

        roleDTO = new RoleDTO(1L, "TEST", new ArrayList<>());
        rolesDTO = Collections.singletonList(roleDTO);
    }

    @Test
    public void givenRoleId_whenGetRole_thenReturnDto() throws Exception {
        given(roleRepository.findOne(1L)).willReturn(role);
        given(roleMapper.toDto(role)).willReturn(roleDTO);

        assertEquals(roleService.getRole(1L), roleDTO);
    }

    @Test
    public void whenGetRoles_thenReturnDtoList() throws Exception {
        given(roleRepository.findAll(defaultPageRequest)).willReturn(rolesPage);
        given(roleMapper.roleListToDto(rolesPage)).willReturn(rolesDTO);

        assertEquals(roleService.getRoleList(defaultPageRequest), rolesDTO);
    }
}