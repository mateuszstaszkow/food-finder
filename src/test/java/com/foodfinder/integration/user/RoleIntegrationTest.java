package com.foodfinder.integration.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.integration.config.IntegrationTestSetup;
import com.foodfinder.user.domain.dto.RoleDTO;
import com.foodfinder.user.repository.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class RoleIntegrationTest extends IntegrationTestSetup {

    @Autowired
    private RoleRepository roleRepository;
    
    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
    public void whenGetRoles_thenReturnsStatusOk() throws Exception {
        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
    public void givenRole_whenPostRole_thenReturnsStatusCreated() throws Exception {
        RoleDTO role = new RoleDTO(1L, "TEST", new ArrayList<>());
        String jsonRole = new ObjectMapper().writeValueAsString(role);

        mockMvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRole))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
    public void givenRoleWithNullId_whenPostRole_thenReturnsStatusCreated() throws Exception {
        RoleDTO role = new RoleDTO(1L, "TEST", new ArrayList<>());
        String jsonRole = new ObjectMapper().writeValueAsString(role);

        mockMvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRole))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
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

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenRole_whenUpdateRole_thenNotAddANewOne() throws Exception {
        addRoleToDatabase();

        long before = roleRepository.count();

        String result = mockMvc.perform(get("/api/roles")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        RoleDTO role = (RoleDTO) ((ArrayList)(mapper.readValue(result, new TypeReference<List<RoleDTO>>(){}))).get(0);
        String jsonRole = new ObjectMapper().writeValueAsString(role);

        mockMvc.perform(post("/api/roles/" + role.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRole))
                .andExpect(status().isOk());

        assertEquals(before, roleRepository.count());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenRole_whenUpdateRole_thenUpdateNameField() throws Exception {
        addRoleToDatabase();

        String result = mockMvc.perform(get("/api/roles")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        RoleDTO role = (RoleDTO) ((ArrayList)(mapper.readValue(result, new TypeReference<List<RoleDTO>>(){}))).get(0);
        role.setName("UPDATED");
        String jsonRole = new ObjectMapper().writeValueAsString(role);

        mockMvc.perform(post("/api/roles/" + role.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRole))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/roles/" + role.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(role.getName())));
    }

    private void addRoleToDatabase() throws Exception {
        RoleDTO role = new RoleDTO(null, "TEST", new ArrayList<>());
        String jsonRole = new ObjectMapper().writeValueAsString(role);

        mockMvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRole))
                .andExpect(status().isCreated());
    }
}