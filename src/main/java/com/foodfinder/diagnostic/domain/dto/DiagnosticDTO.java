package com.foodfinder.diagnostic.domain.dto;

import lombok.Data;

@Data
public class DiagnosticDTO {

    private Long id;
    private String message;
    private String exception;
}
