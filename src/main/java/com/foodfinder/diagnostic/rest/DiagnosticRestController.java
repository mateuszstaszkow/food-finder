package com.foodfinder.diagnostic.rest;

import com.foodfinder.diagnostic.DiagnosticService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/${food-finder.prefix}")
class DiagnosticRestController {

    private final DiagnosticService diagnosticService;

    @RequestMapping(value = "/diagnostic", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getDiagnosticList() {
        return diagnosticService.getDiagnosticList();
    }

}