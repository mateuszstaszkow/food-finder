package com.foodfinder.diagnostic.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.container.exceptions.rest.ExceptionRestController;
import com.foodfinder.diagnostic.domain.dto.DiagnosticDTO;
import com.foodfinder.diagnostic.service.DiagnosticService;
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
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DiagnosticRestController.class)
@EnableSpringDataWebSupport
public class DiagnosticRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DiagnosticService diagnosticService;

    @MockBean
    private ExceptionRestController exceptionRestController;

    @Test
    @WithMockUser("VIEW_ADMIN")
    public void givenDiagnostic_whenGetDiagnostic_thenReturnJson() throws Exception {

        DiagnosticDTO diagnostic = DiagnosticDTO.builder()
                .id(1L)
                .message("Special Error")
                .exception("Stack trace")
                .comments("Unexpected exception")
                .date(new Date(0))
                .build();

        given(diagnosticService.getDiagnostic(1L)).willReturn(diagnostic);

        mvc.perform(get("/api/diagnostics/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(diagnostic.getMessage())));
    }

    @Test
    @WithMockUser("VIEW_ADMIN")
    public void givenDiagnostics_whenGetDiagnostics_thenReturnJsonArray() throws Exception {

        DiagnosticDTO diagnostic = DiagnosticDTO.builder()
                .id(1L)
                .message("Special Error")
                .exception("Stack trace")
                .comments("Unexpected exception")
                .date(new Date(0))
                .build();
        List<DiagnosticDTO> allDiagnostics = Collections.singletonList(diagnostic);
        PageRequest defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        given(diagnosticService.getDiagnosticList(defaultPageRequest)).willReturn(allDiagnostics);

        mvc.perform(get("/api/diagnostics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].message", is(diagnostic.getMessage())));
    }

    @Test
    @WithMockUser("VIEW_ADMIN")
    public void givenDiagnostic_whenAddDiagnostic_thenReturnStatusCreated() throws Exception {

        DiagnosticDTO diagnostic = DiagnosticDTO.builder()
                .id(1L)
                .message("Special Error")
                .exception("Stack trace")
                .comments("Unexpected exception")
                .date(new Date(0))
                .build();
        String jsonDiagnostic = new ObjectMapper().writeValueAsString(diagnostic);

        mvc.perform(post("/api/diagnostics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDiagnostic))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenUnauthorizedUser_thenReturnStatusUnauthorized() throws Exception {

        mvc.perform(get("/api/diagnostics"))
                .andExpect(status().isUnauthorized());
    }
}