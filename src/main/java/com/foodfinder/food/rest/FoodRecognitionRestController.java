package com.foodfinder.food.rest;

import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.food.service.FoodRecognitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/${food-finder.prefix}/recognize")
class FoodRecognitionRestController {

    private final FoodRecognitionService recognitionService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO recognizeFood(@RequestParam(value = "file") MultipartFile file) {
        return recognitionService.recognizeFood(file);
    }
}