package com.foodfinder.diet.rest;

import com.foodfinder.diet.service.DietService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/${food-finder.prefix}")
class DietRestController {

    private final DietService dietService;

    @RequestMapping("/diets")
    @ResponseStatus(HttpStatus.OK)
    public String getFoodList() {
        return dietService.getFoodList();
    }
}