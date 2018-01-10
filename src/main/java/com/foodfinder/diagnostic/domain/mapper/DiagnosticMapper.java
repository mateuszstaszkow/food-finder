package com.foodfinder.diagnostic.domain.mapper;

import com.foodfinder.diagnostic.domain.entity.Diagnostic;
import com.foodfinder.diagnostic.domain.dto.DiagnosticDTO;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiagnosticMapper {

    Diagnostic toEntity(DiagnosticDTO diagnosticDTO);

    DiagnosticDTO toDto(Diagnostic diagnostic);

    List<DiagnosticDTO> diagnosticListToDto(Page<Diagnostic> diagnostic);
}
