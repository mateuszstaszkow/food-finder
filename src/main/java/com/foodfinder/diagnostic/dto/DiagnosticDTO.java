package com.foodfinder.diagnostic.dto;

import lombok.Data;

@Data
public class DiagnosticDTO {

    private Long id;
    private String message;
    private String exception;
}
