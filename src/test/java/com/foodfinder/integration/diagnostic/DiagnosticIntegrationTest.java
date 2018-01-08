package com.foodfinder.integration.diagnostic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.diagnostic.domain.dto.DiagnosticDTO;
import com.foodfinder.integration.config.IntegrationTestSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class DiagnosticIntegrationTest extends IntegrationTestSetup {

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void whenGetDiagnostics_thenReturnsStatusOk() throws Exception {
        mockMvc.perform(get("/api/diagnostics"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenDiagnostic_whenPostDiagnostic_thenReturnsStatusCreated() throws Exception {
        DiagnosticDTO diagnostic = DiagnosticDTO.builder()
                .id(1L)
                .message("Special Error")
                .exception("Stack trace")
                .comments("Unexpected exception")
                .date(new Date(0))
                .build();
        String jsonDiagnostic = new ObjectMapper().writeValueAsString(diagnostic);

        mockMvc.perform(post("/api/diagnostics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDiagnostic))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenDiagnosticWithNullId_whenPostDiagnostic_thenReturnsStatusCreated() throws Exception {
        DiagnosticDTO diagnostic = DiagnosticDTO.builder()
                .id(1L)
                .message("Special Error")
                .exception("Stack trace")
                .comments("Unexpected exception")
                .date(new Date(0))
                .build();
        String jsonDiagnostic = new ObjectMapper().writeValueAsString(diagnostic);

        mockMvc.perform(post("/api/diagnostics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDiagnostic))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenDiagnostic_whenGetDiagnostic_thenReturnsDiagnostic() throws Exception {
        DiagnosticDTO diagnostic = DiagnosticDTO.builder()
                .id(1L)
                .message("Special Error")
                .exception("Stack trace")
                .comments("Unexpected exception")
                .date(new Date(0))
                .build();
        String jsonDiagnostic = new ObjectMapper().writeValueAsString(diagnostic);

        mockMvc.perform(post("/api/diagnostics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDiagnostic))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/diagnostics/" + diagnostic.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(diagnostic.getMessage())));
    }
}