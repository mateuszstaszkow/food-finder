package com.foodfinder.translator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional
public class TranslatorService {

    private final FoodGroupsTranslator foodGroupsTranslator;
    private final ProductsTranslator productsTranslator;
    private final CompositionTranslator compositionTranslator;

    public void translateFoodGroups() {
        foodGroupsTranslator.translate();
    }

    public void translateProducts() {
        productsTranslator.translate();
    }

    public void translateComposition() {
        compositionTranslator.translate();
    }

    public void translateAll() {
        Arrays.asList(foodGroupsTranslator, productsTranslator, compositionTranslator)
                .forEach(Translator::translate);
    }
}
