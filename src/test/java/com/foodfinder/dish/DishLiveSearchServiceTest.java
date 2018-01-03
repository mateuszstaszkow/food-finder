package com.foodfinder.dish;

import com.foodfinder.dish.domain.dto.DishDTO;
import com.foodfinder.dish.domain.entity.Dish;
import com.foodfinder.dish.domain.mapper.DishMapper;
import com.foodfinder.dish.repository.DishRepository;
import com.foodfinder.dish.service.DishLiveSearchService;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.food.domain.entity.Product;
import com.foodfinder.food.domain.mapper.FoodMapper;
import com.foodfinder.food.service.FoodLiveSearchService;
import com.foodfinder.food.service.FoodTranslationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class DishLiveSearchServiceTest {

    @MockBean
    private DishRepository dishRepository;

    @MockBean
    private DishMapper dishMapper;

    @MockBean
    private FoodTranslationService translationService;

    @MockBean
    private FoodLiveSearchService foodLiveSearchService;

    @MockBean
    private FoodMapper foodMapper;

    private DishLiveSearchService dishLiveSearchService;
    private List<DishDTO> dishesDTO;
    private List<Dish> dishes;

    @Before
    public void setup() {
        dishLiveSearchService = new DishLiveSearchService(dishRepository, dishMapper, translationService,
                foodLiveSearchService, foodMapper);

        Dish dish = Dish.builder()
                .id(1L)
                .name("Dumplings")
                .description("Traditional Russian with greaves")
                .products(new HashSet<>(Collections.singletonList(new Product())))
                .hits(1L)
                .translatedName("Pierogi")
                .translatedDescription("Tradycyjne ruskie ze skwarkami")
                .build();
        dishes = Collections.singletonList(dish);

        DishDTO dishDTO = DishDTO.builder()
                .id(1L)
                .name("Dumplings")
                .description("Traditional Russian with greaves")
                .products(Collections.singletonList(new ProductDTO()))
                .hits(1L)
                .build();
        dishesDTO = Collections.singletonList(dishDTO);
    }

    @Test
    public void givenEnglishName_whenGetDishes_thenReturnEnglishDtoList() throws Exception {
        String dishName = "Dumplings";

        given(translationService.isPolishLanguage()).willReturn(false);
        given(dishRepository.findByNameStartsWithAndIsCondition(dishName, " raw")).willReturn(dishes);
        given(dishMapper.dishListToDto(dishes)).willReturn(dishesDTO);
        given(dishRepository.findByNameStartsWith(dishName)).willReturn(new ArrayList<>());
        given(dishRepository.findTop10ByNameContaining(dishName)).willReturn(new ArrayList<>());

        assertEquals(dishLiveSearchService.getDishes(dishName, 10), dishesDTO);
    }

}
