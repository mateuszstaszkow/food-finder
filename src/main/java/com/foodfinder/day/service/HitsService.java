package com.foodfinder.day.service;

import com.foodfinder.day.domain.entity.Day;
import com.foodfinder.day.domain.entity.TimedDish;
import com.foodfinder.dish.domain.entity.Dish;
import com.foodfinder.dish.repository.DishRepository;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.food.domain.entity.Product;
import com.foodfinder.food.domain.mapper.FoodMapper;
import com.foodfinder.food.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HitsService {

    private final ProductRepository productRepository;
    private final DishRepository dishRepository;
    private final FoodMapper foodMapper;

    public Day incrementHitsForANewDay(Day day) {
        Set<TimedDish> timedDishes = day.getTimedDishes()
                .stream()
                .map(this::incrementHits)
                .collect(Collectors.toSet());

        day.setTimedDishes(timedDishes);

        return day;
    }

    public Day incrementHitsForAnUpdatedDay(Day day) {
        Set<TimedDish> timedDishes = day.getTimedDishes();
        List<Dish> dbDishes = getDatabaseDishes(timedDishes);

        timedDishes = timedDishes.stream()
                .map(timedDish -> checkIfExistsAndIncrementHits(timedDish, dbDishes))
                .collect(Collectors.toSet());
        day.setTimedDishes(timedDishes);

        return day;
    }

    public void incrementHitsAndSave(ProductDTO productDTO) {
        Product productEntity = foodMapper.toEntity(productDTO);
        productEntity = incrementHits(productEntity);
        productRepository.save(productEntity);
    }

    private TimedDish checkIfExistsAndIncrementHits(TimedDish timedDish, List<Dish> dbDishes) {
        Dish dish = timedDish.getDish();

        if(dbDishes.contains(dish)) {
            return timedDish;
        }

        List<Product> dbProducts = getDatabaseProducts(dish);
        Set<Product> products = dish.getProducts()
                .stream()
                .map(product -> checkIfExistsAndIncrementHits(product, dbProducts))
                .collect(Collectors.toSet());

        dish.setProducts(products);
        Long hits = Optional.ofNullable(dish.getHits())
                .map(h -> h + 1)
                .orElse(1L);
        dish.setHits(hits);
        timedDish.setDish(dish);

        return timedDish;
    }

    private Product checkIfExistsAndIncrementHits(Product product, List<Product> dbProducts) {
        if(dbProducts.contains(product)) {
            return product;
        }

        Long hits = Optional.ofNullable(product.getHits())
                .map(h -> h + 1)
                .orElse(1L);
        product.setHits(hits);

        return product;
    }

    private TimedDish incrementHits(TimedDish timedDish) {
        Dish dish = timedDish.getDish();
        Set<Product> products = dish.getProducts()
                .stream()
                .map(this::incrementHits)
                .collect(Collectors.toSet());

        dish.setProducts(products);
        Long hits = Optional.ofNullable(dish.getHits())
                .map(h -> h + 1)
                .orElse(1L);
        dish.setHits(hits);
        timedDish.setDish(dish);

        return timedDish;
    }

    private Product incrementHits(Product product) {
        Long hits = Optional.ofNullable(product.getHits())
                .map(h -> h + 1)
                .orElse(1L);
        product.setHits(hits);

        return product;
    }

    private List<Dish> getDatabaseDishes(Set<TimedDish> timedDishes) {
        List<Dish> updatedDishes = timedDishes.stream()
                .map(TimedDish::getDish)
                .collect(Collectors.toList());
        List<Long> dishIds = updatedDishes.stream()
                .map(Dish::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return dishRepository.findByIdIn(dishIds);
    }

    private List<Product> getDatabaseProducts(Dish dish) {
        List<Long> productIds = dish.getProducts()
                .stream()
                .map(Product::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return productRepository.findByIdIn(productIds);
    }
}
