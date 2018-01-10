package com.foodfinder.migrator.rest;

import com.foodfinder.container.exceptions.rest.ExceptionRestController;
import com.foodfinder.migrator.service.MigratorService;
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
@WebMvcTest(MigratorRestController.class)
@EnableSpringDataWebSupport
public class MigratorRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MigratorService migratorService;

    @MockBean
    private ExceptionRestController exceptionRestController;

    @Test
    @WithMockUser
    public void whenMigrateProducts_thenReturnStatusOk() throws Exception {

        mvc.perform(post("/api/migrate/products"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void whenMigrateGroups_thenReturnStatusOk() throws Exception {

        mvc.perform(post("/api/migrate/groups"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void whenMigrateAll_thenReturnStatusOk() throws Exception {

        mvc.perform(post("/api/migrate/all"))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUnauthorizedUser_whenMigrateProducts_thenReturnStatusOk() throws Exception {

        mvc.perform(post("/api/migrate/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenUnauthorizedUser_whenMigrateGroups_thenReturnStatusOk() throws Exception {

        mvc.perform(post("/api/migrate/groups"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenUnauthorizedUser_whenMigrateAll_thenReturnStatusOk() throws Exception {

        mvc.perform(post("/api/migrate/all"))
                .andExpect(status().isUnauthorized());
    }
}
