package com.foodfinder.translator.rest;

import com.foodfinder.container.exceptions.rest.ExceptionRestController;
import com.foodfinder.translator.service.TranslatorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TranslatorRestController.class)
@EnableSpringDataWebSupport
public class TranslatorRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TranslatorService translatorService;

    @MockBean
    private ExceptionRestController exceptionRestController;

    @Test
    @WithMockUser
    public void whenTranslateProducts_thenReturnStatusOk() throws Exception {

        mvc.perform(post("/api/translate/products"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void whenTranslateGroups_thenReturnStatusOk() throws Exception {

        mvc.perform(post("/api/translate/groups"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void whenTranslateComposition_thenReturnStatusOk() throws Exception {

        mvc.perform(post("/api/translate/composition"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void whenTranslateAll_thenReturnStatusOk() throws Exception {

        mvc.perform(post("/api/translate/all"))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUnauthorizedUser_whenTranslateProducts_thenReturnStatusOk() throws Exception {

        mvc.perform(post("/api/translate/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenUnauthorizedUser_whenTranslateGroups_thenReturnStatusOk() throws Exception {

        mvc.perform(post("/api/translate/groups"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenUnauthorizedUser_whenTranslateComposition_thenReturnStatusOk() throws Exception {

        mvc.perform(post("/api/translate/composition"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenUnauthorizedUser_whenTranslateAll_thenReturnStatusOk() throws Exception {

        mvc.perform(post("/api/translate/all"))
                .andExpect(status().isUnauthorized());
    }
}