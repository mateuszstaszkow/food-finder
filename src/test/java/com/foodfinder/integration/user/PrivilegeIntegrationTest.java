package com.foodfinder.integration.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.integration.config.IntegrationTestSetup;
import com.foodfinder.user.domain.dto.PrivilegeDTO;
import com.foodfinder.user.repository.PrivilegeRepository;
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
public class PrivilegeIntegrationTest extends IntegrationTestSetup {

    @Autowired
    private PrivilegeRepository privilegeRepository;
    
    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
    public void whenGetPrivileges_thenReturnsStatusOk() throws Exception {
        mockMvc.perform(get("/api/privileges"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
    public void givenPrivilege_whenPostPrivilege_thenReturnsStatusCreated() throws Exception {
        PrivilegeDTO privilege = new PrivilegeDTO(1L, "VIEW_TEST");
        String jsonPrivilege = new ObjectMapper().writeValueAsString(privilege);

        mockMvc.perform(post("/api/privileges")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPrivilege))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
    public void givenPrivilegeWithNullId_whenPostPrivilege_thenReturnsStatusCreated() throws Exception {
        PrivilegeDTO privilege = new PrivilegeDTO(1L, "VIEW_TEST");
        String jsonPrivilege = new ObjectMapper().writeValueAsString(privilege);

        mockMvc.perform(post("/api/privileges")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPrivilege))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
    public void givenPrivilege_whenGetPrivilege_thenReturnsPrivilege() throws Exception {
        PrivilegeDTO privilege = new PrivilegeDTO(1L, "VIEW_TEST");
        String jsonPrivilege = new ObjectMapper().writeValueAsString(privilege);

        mockMvc.perform(post("/api/privileges")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPrivilege))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/privileges/" + privilege.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(privilege.getName())));
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenPrivilege_whenUpdatePrivilege_thenNotAddANewOne() throws Exception {
        addPrivilegeToDatabase();

        long before = privilegeRepository.count();

        String result = mockMvc.perform(get("/api/privileges")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        PrivilegeDTO privilege = (PrivilegeDTO) ((ArrayList)(mapper.readValue(result, new TypeReference<List<PrivilegeDTO>>(){}))).get(0);
        String jsonPrivilege = new ObjectMapper().writeValueAsString(privilege);

        mockMvc.perform(post("/api/privileges/" + privilege.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPrivilege))
                .andExpect(status().isOk());

        assertEquals(before, privilegeRepository.count());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenPrivilege_whenUpdatePrivilege_thenUpdateNameField() throws Exception {
        addPrivilegeToDatabase();

        String result = mockMvc.perform(get("/api/privileges")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        PrivilegeDTO privilege = (PrivilegeDTO) ((ArrayList)(mapper.readValue(result, new TypeReference<List<PrivilegeDTO>>(){}))).get(0);
        privilege.setName("UPDATED");
        String jsonPrivilege = new ObjectMapper().writeValueAsString(privilege);

        mockMvc.perform(post("/api/privileges/" + privilege.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPrivilege))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/privileges/" + privilege.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(privilege.getName())));
    }

    private void addPrivilegeToDatabase() throws Exception {
        PrivilegeDTO privilege = new PrivilegeDTO(null, "VIEW_TEST");
        String jsonPrivilege = new ObjectMapper().writeValueAsString(privilege);

        mockMvc.perform(post("/api/privileges")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPrivilege))
                .andExpect(status().isCreated());
    }
}