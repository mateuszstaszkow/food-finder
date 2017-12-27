package com.foodfinder.diagnostic.service;

import com.foodfinder.diagnostic.dao.DiagnosticRepository;
import com.foodfinder.diagnostic.domain.dto.DiagnosticDTO;
import com.foodfinder.diagnostic.domain.entity.Diagnostic;
import com.foodfinder.diagnostic.domain.mapper.DiagnosticMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class DiagnosticService {

    private final DiagnosticRepository diagnosticRepository;
    private final DiagnosticMapper diagnosticMapper;

    public List<DiagnosticDTO> getDiagnosticList(Pageable pageable) {
        return Optional.ofNullable(diagnosticRepository.findAll(pageable))
                .map(diagnosticMapper::diagnosticListToDto)
                .orElseThrow(NotFoundException::new);
    }

    public DiagnosticDTO getDiagnostic(Long id) {
        return Optional.ofNullable(diagnosticRepository.findOne(id))
                .map(diagnosticMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    public void postDiagnostic(DiagnosticDTO diagnostic) {
        Diagnostic diagnosticEntity = Optional.ofNullable(diagnostic)
                .map(diagnosticMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        diagnosticRepository.save(diagnosticEntity);
    }

    public void updateDiagnostic(Long id, DiagnosticDTO diagnostic) {
        Diagnostic diagnosticEntity = Optional.ofNullable(diagnostic)
                .map(diagnosticMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        diagnosticEntity.setId(id);
        diagnosticRepository.save(diagnosticEntity);
    }
}