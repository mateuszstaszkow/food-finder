package com.foodfinder.integration.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.integration.config.IntegrationTestSetup;
import com.foodfinder.user.domain.dto.RoleDTO;
import com.foodfinder.user.domain.dto.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserIntegrationTest extends IntegrationTestSetup {

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void whenGetUsers_thenReturnsStatusOk() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenUser_whenPostUser_thenReturnsStatusCreated() throws Exception {
        UserDTO user = UserDTO.builder()
                .id(1L)
                .name("User")
                .surname("Userski")
                .email("user1@foodfinder.com")
                .role(new RoleDTO(2L))
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .days(Collections.singletonList(new DayDTO()))
                .build();
        String jsonUser = new ObjectMapper().writeValueAsString(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenUserWithNullId_whenPostUser_thenReturnsStatusCreated() throws Exception {
        UserDTO user = UserDTO.builder()
                .id(null)
                .name("User")
                .surname("Userski")
                .email("userx@foodfinder.com")
                .role(new RoleDTO(2L))
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .days(Collections.singletonList(new DayDTO()))
                .build();
        String jsonUser = new ObjectMapper().writeValueAsString(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenUser_whenGetUser_thenReturnsUser() throws Exception {
        UserDTO user = UserDTO.builder()
                .id(3L)
                .name("User")
                .surname("Userski")
                .email("user3@foodfinder.com")
                .role(new RoleDTO(2L))
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .days(Collections.singletonList(new DayDTO()))
                .build();
        String jsonUser = new ObjectMapper().writeValueAsString(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())));
    }
}