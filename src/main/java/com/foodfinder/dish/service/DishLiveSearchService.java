package com.foodfinder.dish.service;

import com.foodfinder.dish.repository.DishRepository;
import com.foodfinder.dish.domain.dto.DishDTO;
import com.foodfinder.dish.domain.entity.Dish;
import com.foodfinder.dish.domain.mapper.DishMapper;
import com.foodfinder.food.domain.entity.Product;
import com.foodfinder.food.domain.mapper.FoodMapper;
import com.foodfinder.food.service.FoodLiveSearchService;
import com.foodfinder.food.service.FoodTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class DishLiveSearchService {

    private final DishRepository dishRepository;
    private final DishMapper dishMapper;
    private final FoodTranslationService translationService;
    private final FoodLiveSearchService foodLiveSearchService;
    private final FoodMapper foodMapper;

    private static final String PRODUCT_TYPE = " raw";
    private static final String PRODUCT_TYPE_PL = " surow";

    public List<DishDTO> getDishes(String name, int limit) {
        if(translationService.isPolishLanguage()) {
            return getTranslatedDishes(name, limit);
        }
        return getOriginalDishes(name, limit);
    }

    private List<DishDTO> getOriginalDishes(String name, int limit) {
        return Optional.ofNullable(dishRepository.findByNameStartsWithAndIsCondition(name, PRODUCT_TYPE))
                .map(this::getSortedDTOs)
                .map(p -> getAdditionalDishes(limit, p, dishRepository.findByNameStartsWith(name)))
                .map(p -> getAdditionalDishes(limit, p, dishRepository.findTop10ByNameContaining(name)))
                .map(p -> getAdditionalDishes(limit, p, getDishesByProducts(name, limit, p.size())))
                .map(p -> truncateDishList(p, limit))
                .orElseThrow(NotFoundException::new);
    }

    private List<DishDTO> getTranslatedDishes(String name, int limit) {
        return Optional.ofNullable(dishRepository.findByTranslatedNameStartsWithAndIsCondition(name, PRODUCT_TYPE_PL))
                .map(this::getSortedDTOs)
                .map(p -> getAdditionalDishes(limit, p, dishRepository.findByTranslatedNameStartsWith(name)))
                .map(p -> getAdditionalDishes(limit, p, dishRepository.findTop10ByTranslatedNameContaining(name)))
                .map(p -> truncateDishList(p, limit))
                .orElseThrow(NotFoundException::new);
    }

    private List<Dish> getDishesByProducts(String name, int limit, int currentDishesCount) {
        if(currentDishesCount >= limit) {
            return new ArrayList<>();
        }

        return Optional.ofNullable(foodLiveSearchService.getProducts(name, limit))
                .map(foodMapper::productListToEntity)
                .map(this::mapProductListToDishList)
                .orElseThrow(NotFoundException::new);
    }

    private List<Dish> mapProductListToDishList(List<Product> products) {
        return products.stream()
                .map(dishRepository::findByProducts)
                .collect(Collectors.toList());
    }

    private List<DishDTO> getAdditionalDishes(int limit, List<DishDTO> rawDishes, List<Dish> dishDbList) {
        if(rawDishes.size() >= limit) {
            return rawDishes;
        }

        List<DishDTO> additionalDishes = getSortedDTOs(dishDbList);
        int toMaximum = limit - rawDishes.size();
        int dishesToFind = toMaximum < additionalDishes.size() ? toMaximum : additionalDishes.size();

        IntStream.range(0, dishesToFind)
                .forEach(dishNo -> {
                    DishDTO additionalDish = additionalDishes.get(dishNo);
                    if(!rawDishes.contains(additionalDish)) {
                        rawDishes.add(additionalDish);
                    }
                });

        return rawDishes;
    }

    private List<DishDTO> getSortedDTOs(List<Dish> dishDbList) {
        return Optional.ofNullable(dishDbList)
                .map(dishes -> {
                    dishes.sort(Comparator.comparingInt(dish -> dish.getName().length()));
                    return dishes;
                })
                .map(dishMapper::dishListToDto)
                .orElse(new ArrayList<>());
    }

    private List<DishDTO> truncateDishList(List<DishDTO> dishes, int limit) {
        return dishes.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}
