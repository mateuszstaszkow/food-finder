package com.foodfinder.diagnostic.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DiagnosticDTO {

    private Long id;
    private String message;
    private String exception;
    private String comments;
    private Date date;
}
