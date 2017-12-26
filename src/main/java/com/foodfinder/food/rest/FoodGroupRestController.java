package com.foodfinder.food.rest;

import com.foodfinder.food.domain.dto.FoodGroupDTO;
import com.foodfinder.food.service.FoodGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/${food-finder.prefix}/groups")
class FoodGroupRestController {

    private final FoodGroupService foodGroupService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<FoodGroupDTO> getFoodGroupList(Pageable pageable, @RequestParam(value="name", required = false) String name) {
        return foodGroupService.getFoodGroupList(pageable, name);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addFoodGroup(@RequestBody FoodGroupDTO foodGroupDTO) {
        foodGroupService.postFoodGroup(foodGroupDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public FoodGroupDTO getFoodGroup(@PathVariable Long id) {
        return foodGroupService.getFoodGroup(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateFoodGroup(@PathVariable Long id, @RequestBody FoodGroupDTO foodGroupDTO) {
        foodGroupService.updateFoodGroup(id, foodGroupDTO);
    }
}