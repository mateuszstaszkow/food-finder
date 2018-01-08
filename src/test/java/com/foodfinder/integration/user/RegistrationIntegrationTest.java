package com.foodfinder.integration.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.integration.config.IntegrationTestSetup;
import com.foodfinder.user.domain.dto.RegistrationDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class RegistrationIntegrationTest extends IntegrationTestSetup {

    @Test
    public void givenRegistrationDTOWithoutAuthentication_whenRegister_thenReturnStatusCreated() throws Exception {
        RegistrationDTO account = RegistrationDTO.builder()
                .name("New User")
                .surname("Accountski")
                .email("account@account.com")
                .password("encryptedpassword")
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .build();
        String jsonAccount = new ObjectMapper().writeValueAsString(account);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAccount))
                .andExpect(status().isCreated());
    }
}