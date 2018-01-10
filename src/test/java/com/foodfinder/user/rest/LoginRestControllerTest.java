package com.foodfinder.user.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.container.exceptions.rest.ExceptionRestController;
import com.foodfinder.user.domain.dto.RegistrationDTO;
import com.foodfinder.user.service.LoginService;
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
@WebMvcTest(LoginRestController.class)
@EnableSpringDataWebSupport
public class LoginRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LoginService loginService;

    @MockBean
    private ExceptionRestController exceptionRestController;

    @Test
    @WithMockUser
    public void givenRegistrationDTO_whenRegister_thenReturnStatusCreated() throws Exception {

        RegistrationDTO account = new RegistrationDTO("new@user.com", "password");
        String jsonAccount = new ObjectMapper().writeValueAsString(account);

        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAccount))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void givenMockUser_whenLogin_thenReturnStatusOk() throws Exception {
        mvc.perform(post("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoUser_whenLogin_thenReturnStatusUnauthorized() throws Exception {
        mvc.perform(post("/login"))
                .andExpect(status().isUnauthorized());
    }
}
