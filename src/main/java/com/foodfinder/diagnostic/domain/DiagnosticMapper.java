package com.foodfinder.diagnostic.domain;

import com.foodfinder.diagnostic.dto.DiagnosticDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiagnosticMapper {

    Diagnostic toEntity(DiagnosticDTO diagnosticDTO);

    DiagnosticDTO toDto(Diagnostic diagnostic);
}
