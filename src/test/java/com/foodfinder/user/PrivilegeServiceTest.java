package com.foodfinder.user;

import com.foodfinder.user.domain.dto.PrivilegeDTO;
import com.foodfinder.user.domain.entity.Privilege;
import com.foodfinder.user.domain.mapper.UserMapper;
import com.foodfinder.user.repository.PrivilegeRepository;
import com.foodfinder.user.service.PrivilegeService;
import com.foodfinder.utils.RestControllerTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class PrivilegeServiceTest {

    @MockBean
    private PrivilegeRepository privilegeRepository;

    @MockBean
    private UserMapper privilegeMapper;

    private PrivilegeService privilegeService;
    private PageRequest defaultPageRequest;
    private Privilege privilege;
    private PrivilegeDTO privilegeDTO;
    private List<PrivilegeDTO> privilegesDTO;
    private Page<Privilege> privilegesPage;

    @Before
    public void setup() {
        privilegeService = new PrivilegeService(privilegeRepository, privilegeMapper);
        defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        privilege = new Privilege(1L, "VIEW_TEST");
        privilegesPage = new PageImpl<>(Collections.singletonList(privilege));

        privilegeDTO = new PrivilegeDTO(1L, "VIEW_TEST");
        privilegesDTO = Collections.singletonList(privilegeDTO);
    }

    @Test
    public void givenPrivilegeId_whenGetPrivilege_thenReturnDto() throws Exception {
        given(privilegeRepository.findOne(1L)).willReturn(privilege);
        given(privilegeMapper.toDto(privilege)).willReturn(privilegeDTO);

        assertEquals(privilegeService.getPrivilege(1L), privilegeDTO);
    }

    @Test
    public void whenGetPrivileges_thenReturnDtoList() throws Exception {
        given(privilegeRepository.findAll(defaultPageRequest)).willReturn(privilegesPage);
        given(privilegeMapper.privilegeListToDto(privilegesPage)).willReturn(privilegesDTO);

        assertEquals(privilegeService.getPrivilegeList(defaultPageRequest), privilegesDTO);
    }
}