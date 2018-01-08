package com.foodfinder.user.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.container.exceptions.rest.ExceptionRestController;
import com.foodfinder.user.domain.dto.RegistrationDTO;
import com.foodfinder.user.service.RegistrationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationRestController.class)
@EnableSpringDataWebSupport
public class RegistrationRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private ExceptionRestController exceptionRestController;

    @Test
    @WithMockUser
    public void givenRegistrationDTO_whenRegister_thenReturnStatusCreated() throws Exception {

        RegistrationDTO account = RegistrationDTO.builder()
                .name("User")
                .surname("Userski")
                .email("user@user.com")
                .password("encryptedpassword")
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .build();
        String jsonAccount = new ObjectMapper().writeValueAsString(account);

        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAccount))
                .andExpect(status().isCreated());
    }
}
