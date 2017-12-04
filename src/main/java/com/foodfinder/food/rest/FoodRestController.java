package com.foodfinder.food.rest;

import com.foodfinder.food.DishService;
import com.foodfinder.food.ProductService;
import com.foodfinder.food.dto.DishDTO;
import com.foodfinder.food.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/${food-finder.prefix}")
class FoodRestController {

    private final ProductService productService;
    private final DishService dishService;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> getProductList() {
        return productService.getProductList();
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addProduct(@RequestBody ProductDTO productDTO) {
        productService.postProduct(productDTO);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        productService.updateProduct(id, productDTO);
    }

    @RequestMapping(value = "/dishes", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<DishDTO> getDishList() {
        return dishService.getDishList();
    }
}