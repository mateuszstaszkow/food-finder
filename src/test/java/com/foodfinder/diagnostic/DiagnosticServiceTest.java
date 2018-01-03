package com.foodfinder.diagnostic;

import com.foodfinder.diagnostic.domain.dto.DiagnosticDTO;
import com.foodfinder.diagnostic.domain.entity.Diagnostic;
import com.foodfinder.diagnostic.domain.mapper.DiagnosticMapper;
import com.foodfinder.diagnostic.repository.DiagnosticRepository;
import com.foodfinder.diagnostic.service.DiagnosticService;
import com.foodfinder.utils.RestControllerTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class DiagnosticServiceTest {

    @MockBean
    private DiagnosticRepository diagnosticRepository;

    @MockBean
    private DiagnosticMapper diagnosticMapper;

    private DiagnosticService diagnosticService;
    private PageRequest defaultPageRequest;
    private Diagnostic diagnostic;
    private DiagnosticDTO diagnosticDTO;
    private List<DiagnosticDTO> diagnosticsDTO;
    private Page<Diagnostic> diagnosticsPage;

    @Before
    public void setup() {
        diagnosticService = new DiagnosticService(diagnosticRepository, diagnosticMapper);
        defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        diagnostic = Diagnostic.builder()
                .id(1L)
                .message("Special Error")
                .exception("Stack trace")
                .comments("Unexpected exception")
                .date(new Date(0))
                .build();
        diagnosticsPage = new PageImpl<>(Collections.singletonList(diagnostic));

        diagnosticDTO = DiagnosticDTO.builder()
                .id(1L)
                .message("Special Error")
                .exception("Stack trace")
                .comments("Unexpected exception")
                .date(new Date(0))
                .build();
        diagnosticsDTO = Collections.singletonList(diagnosticDTO);
    }

    @Test
    public void givenDiagnosticId_whenGetDiagnostic_thenReturnDto() throws Exception {
        given(diagnosticRepository.findOne(1L)).willReturn(diagnostic);
        given(diagnosticMapper.toDto(diagnostic)).willReturn(diagnosticDTO);

        assertEquals(diagnosticService.getDiagnostic(1L), diagnosticDTO);
    }

    @Test
    public void whenGetDiagnostics_thenReturnDtoList() throws Exception {
        given(diagnosticRepository.findAll(defaultPageRequest)).willReturn(diagnosticsPage);
        given(diagnosticMapper.diagnosticListToDto(diagnosticsPage)).willReturn(diagnosticsDTO);

        assertEquals(diagnosticService.getDiagnosticList(defaultPageRequest), diagnosticsDTO);
    }
}