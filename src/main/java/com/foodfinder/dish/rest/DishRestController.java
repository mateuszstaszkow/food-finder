package com.foodfinder.dish.rest;

import com.foodfinder.dish.domain.dto.DishDTO;
import com.foodfinder.dish.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/${food-finder.prefix}/dishes")
class DishRestController {

    private final DishService dishService;

    @RequestMapping(value = "/dishes", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<DishDTO> getDishList() {
        return dishService.getDishList();
    }

//    @RequestMapping(method = RequestMethod.GET)
//    @ResponseStatus(HttpStatus.OK)
//    public List<DishDTO> getDishList(Pageable pageable) {
//        return dishService.getDishList(pageable);
//    }
//
//    @RequestMapping(method = RequestMethod.POST)
//    @ResponseStatus(HttpStatus.CREATED)
//    public void addProduct(@RequestBody ProductDTO productDTO) {
//        dishService.postProduct(productDTO);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    @ResponseStatus(HttpStatus.OK)
//    public ProductDTO getProduct(@PathVariable Long id) {
//        return dishService.getProduct(id);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
//    @ResponseStatus(HttpStatus.OK)
//    public void updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
//        dishService.updateProduct(id, productDTO);
//    }
}
