package com.foodfinder.translator.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class TranslateDataDTO {

    private List<TranslationDTO> translations;
}
