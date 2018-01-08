package com.foodfinder.integration.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.integration.config.IntegrationTestSetup;
import com.foodfinder.user.domain.dto.RoleDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class RoleIntegrationTest extends IntegrationTestSetup {

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void whenGetRoles_thenReturnsStatusOk() throws Exception {
        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenRole_whenPostRole_thenReturnsStatusCreated() throws Exception {
        RoleDTO role = new RoleDTO(1L, "TEST", new ArrayList<>());
        String jsonRole = new ObjectMapper().writeValueAsString(role);

        mockMvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRole))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenRoleWithNullId_whenPostRole_thenReturnsStatusCreated() throws Exception {
        RoleDTO role = new RoleDTO(1L, "TEST", new ArrayList<>());
        String jsonRole = new ObjectMapper().writeValueAsString(role);

        mockMvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRole))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenRole_whenGetRole_thenReturnsRole() throws Exception {
        RoleDTO role = new RoleDTO(1L, "TEST", new ArrayList<>());
        String jsonRole = new ObjectMapper().writeValueAsString(role);

        mockMvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRole))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/roles/" + role.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(role.getName())));
    }
}