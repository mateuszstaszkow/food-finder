package com.foodfinder.user.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.container.exceptions.rest.ExceptionRestController;
import com.foodfinder.user.domain.dto.RoleDTO;
import com.foodfinder.user.service.RoleService;
import com.foodfinder.utils.RestControllerTestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RoleRestController.class)
@EnableSpringDataWebSupport
public class RoleRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoleService roleService;

    @MockBean
    private ExceptionRestController exceptionRestController;

    @Test
    @WithMockUser
    public void givenRole_whenGetRole_thenReturnJson() throws Exception {

        RoleDTO role = new RoleDTO(1L, "TEST", new ArrayList<>());

        given(roleService.getRole(1L)).willReturn(role);

        mvc.perform(get("/api/roles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(role.getName())));
    }

    @Test
    @WithMockUser
    public void givenRoles_whenGetRoles_thenReturnJsonArray() throws Exception {

        RoleDTO role = new RoleDTO(1L, "TEST", new ArrayList<>());
        List<RoleDTO> allRoles = Collections.singletonList(role);
        PageRequest defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        given(roleService.getRoleList(defaultPageRequest)).willReturn(allRoles);

        mvc.perform(get("/api/roles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(role.getName())));
    }

    @Test
    @WithMockUser
    public void givenRole_whenAddRole_thenReturnStatusCreated() throws Exception {

        RoleDTO role = new RoleDTO(1L, "TEST", new ArrayList<>());
        String jsonRole = new ObjectMapper().writeValueAsString(role);

        mvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRole))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenUnauthorizedUser_thenReturnStatusUnauthorized() throws Exception {

        mvc.perform(get("/api/roles"))
                .andExpect(status().isUnauthorized());
    }
}