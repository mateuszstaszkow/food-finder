package com.foodfinder.translator.rest;

import com.foodfinder.translator.service.TranslatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/${food-finder.prefix}/translate")
public class TranslatorRestController {

    private final TranslatorService translatorService;

    @RequestMapping(value = "/groups", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void translateFoodGroups() {
        translatorService.translateFoodGroups();
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void translateProducts() {
        translatorService.translateProducts();
    }

    @RequestMapping(value = "/composition", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void translateComposition() {
        translatorService.translateComposition();
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void translateAll() {
        translatorService.translateAll();
    }
}
