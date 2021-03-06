package com.foodfinder.integration.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.integration.config.IntegrationTestSetup;
import com.foodfinder.user.domain.dto.RegistrationDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class LoginIntegrationTest extends IntegrationTestSetup {

    @Test
    public void givenRegistrationDTOWithoutAuthentication_whenRegister_thenReturnStatusCreated() throws Exception {
        RegistrationDTO account = new RegistrationDTO("user@user.com", "password");
        String jsonAccount = new ObjectMapper().writeValueAsString(account);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAccount))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenMockUser_whenLogin_thenReturnStatusOk() throws Exception {
        mockMvc.perform(post("/login"))
                .andExpect(status().isOk());
    }

}