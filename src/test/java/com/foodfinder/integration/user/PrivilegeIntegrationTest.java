package com.foodfinder.integration.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.integration.config.IntegrationTestSetup;
import com.foodfinder.user.domain.dto.PrivilegeDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class PrivilegeIntegrationTest extends IntegrationTestSetup {

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void whenGetPrivileges_thenReturnsStatusOk() throws Exception {
        mockMvc.perform(get("/api/privileges"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenPrivilege_whenPostPrivilege_thenReturnsStatusCreated() throws Exception {
        PrivilegeDTO privilege = new PrivilegeDTO(1L, "VIEW_TEST");
        String jsonPrivilege = new ObjectMapper().writeValueAsString(privilege);

        mockMvc.perform(post("/api/privileges")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPrivilege))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenPrivilegeWithNullId_whenPostPrivilege_thenReturnsStatusCreated() throws Exception {
        PrivilegeDTO privilege = new PrivilegeDTO(1L, "VIEW_TEST");
        String jsonPrivilege = new ObjectMapper().writeValueAsString(privilege);

        mockMvc.perform(post("/api/privileges")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPrivilege))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
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
}