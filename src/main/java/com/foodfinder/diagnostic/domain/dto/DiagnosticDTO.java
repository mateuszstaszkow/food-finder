package com.foodfinder.diagnostic.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiagnosticDTO {

    private Long id;
    private String message;
    private String exception;
    private String comments;
    private Date date;
}
