package com.foodfinder.user.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.container.exceptions.rest.ExceptionRestController;
import com.foodfinder.user.domain.dto.PrivilegeDTO;
import com.foodfinder.user.service.PrivilegeService;
import com.foodfinder.utils.RestControllerTestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PrivilegeRestController.class)
@EnableSpringDataWebSupport
public class PrivilegeRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PrivilegeService privilegeService;

    @MockBean
    private ExceptionRestController exceptionRestController;

    @Test
    @WithMockUser
    public void givenPrivilege_whenGetPrivilege_thenReturnJson() throws Exception {

        PrivilegeDTO privilege = new PrivilegeDTO(1L, "VIEW_TEST");
        
        given(privilegeService.getPrivilege(1L)).willReturn(privilege);

        mvc.perform(get("/api/privileges/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(privilege.getName())));
    }

    @Test
    @WithMockUser
    public void givenPrivileges_whenGetPrivileges_thenReturnJsonArray() throws Exception {

        PrivilegeDTO privilege = new PrivilegeDTO(1L, "VIEW_TEST");
        List<PrivilegeDTO> allPrivileges = Collections.singletonList(privilege);
        PageRequest defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        given(privilegeService.getPrivilegeList(defaultPageRequest)).willReturn(allPrivileges);

        mvc.perform(get("/api/privileges")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(privilege.getName())));
    }

    @Test
    @WithMockUser
    public void givenPrivilege_whenAddPrivilege_thenReturnStatusCreated() throws Exception {

        PrivilegeDTO privilege = new PrivilegeDTO(1L, "VIEW_TEST");
        String jsonPrivilege = new ObjectMapper().writeValueAsString(privilege);

        mvc.perform(post("/api/privileges")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPrivilege))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenUnauthorizedUser_thenReturnStatusUnauthorized() throws Exception {

        mvc.perform(get("/api/privileges"))
                .andExpect(status().isUnauthorized());
    }
}