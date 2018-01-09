package com.foodfinder.integration.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.integration.config.IntegrationTestSetup;
import com.foodfinder.user.domain.dto.BasicUserDTO;
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
public class AccountIntegrationTest extends IntegrationTestSetup {

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void whenGetAccounts_thenReturnsStatusOk() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenAccount_whenUpdateAccount_thenReturnsStatusOk() throws Exception {
        BasicUserDTO account = BasicUserDTO.builder()
                .name("User")
                .surname("Userski")
                .email("user@foodfinder.com")
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("man")
                .build();
        String jsonAccount = new ObjectMapper().writeValueAsString(account);

        mockMvc.perform(post("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAccount))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenAccount_whenUpdateAccount_thenReturnsUpdatedDTO() throws Exception {
        BasicUserDTO account = BasicUserDTO.builder()
                .name("UserChanged")
                .surname("Userski")
                .email("user@foodfinder.com")
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("man")
                .build();
        String jsonAccount = new ObjectMapper().writeValueAsString(account);

        mockMvc.perform(post("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAccount))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(account.getName())));
    }
}