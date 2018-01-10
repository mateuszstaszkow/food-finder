package com.foodfinder.diagnostic.rest;

import com.foodfinder.diagnostic.domain.dto.DiagnosticDTO;
import com.foodfinder.diagnostic.service.DiagnosticService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/${food-finder.prefix}/diagnostics")
class DiagnosticRestController {

    private final DiagnosticService diagnosticService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<DiagnosticDTO> getDiagnosticList(Pageable pageable) {
        return diagnosticService.getDiagnosticList(pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addDiagnostic(@RequestBody DiagnosticDTO diagnosticDTO) {
        diagnosticService.postDiagnostic(diagnosticDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DiagnosticDTO getDiagnostic(@PathVariable Long id) {
        return diagnosticService.getDiagnostic(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateDiagnostic(@PathVariable Long id, @RequestBody DiagnosticDTO diagnosticDTO) {
        diagnosticService.updateDiagnostic(id, diagnosticDTO);
    }
}