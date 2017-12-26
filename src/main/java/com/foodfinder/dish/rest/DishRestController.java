package com.foodfinder.dish.rest;

import com.foodfinder.dish.domain.dto.DishDTO;
import com.foodfinder.dish.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/${food-finder.prefix}/dishes")
class DishRestController {

    private final DishService dishService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<DishDTO> getDishList(Pageable pageable, @RequestParam(value="name", required = false) String name) {
        return dishService.getDishList(pageable, name);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addDish(@RequestBody DishDTO dishDTO) {
        dishService.postDish(dishDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DishDTO getDish(@PathVariable Long id) {
        return dishService.getDish(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateDish(@PathVariable Long id, @RequestBody DishDTO dishDTO) {
        dishService.updateDish(id, dishDTO);
    }
}
