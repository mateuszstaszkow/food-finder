package com.foodfinder.dish;

import com.foodfinder.dish.domain.dto.DishDTO;
import com.foodfinder.dish.domain.entity.Dish;
import com.foodfinder.dish.domain.mapper.DishMapper;
import com.foodfinder.dish.repository.DishRepository;
import com.foodfinder.dish.service.DishLiveSearchService;
import com.foodfinder.dish.service.DishService;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.food.domain.entity.Product;
import com.foodfinder.utils.RestControllerTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class DishServiceTest {

    @MockBean
    private DishRepository dishRepository;

    @MockBean
    private DishMapper dishMapper;

    @MockBean
    private DishLiveSearchService dishLiveSearchService;

    private DishService dishService;
    private PageRequest defaultPageRequest;
    private Dish dish;
    private DishDTO dishDTO;
    private List<DishDTO> dishesDTO;
    private Page<Dish> dishesPage;

    @Before
    public void setup() {
        dishService = new DishService(dishRepository, dishLiveSearchService, dishMapper);
        defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        dish = Dish.builder()
                .id(1L)
                .name("Dumplings")
                .description("Traditional Russian with greaves")
                .products(new HashSet<>(Collections.singletonList(new Product())))
                .hits(1L)
                .translatedName("Pierogi")
                .translatedDescription("Tradycyjne ruskie ze skwarkami")
                .build();
        dishesPage = new PageImpl<>(Collections.singletonList(dish));

        dishDTO = DishDTO.builder()
                .id(1L)
                .name("Dumplings")
                .description("Traditional Russian with greaves")
                .products(Collections.singletonList(new ProductDTO()))
                .hits(1L)
                .build();
        dishesDTO = Collections.singletonList(dishDTO);
    }

    @Test
    public void givenDishId_whenGetDish_thenReturnDto() throws Exception {
        given(dishRepository.findOne(1L)).willReturn(dish);
        given(dishMapper.toDto(dish)).willReturn(dishDTO);

        assertEquals(dishService.getDish(1L), dishDTO);
    }

    @Test
    public void whenGetDishes_thenReturnDtoList() throws Exception {
        given(dishRepository.findAll(defaultPageRequest)).willReturn(dishesPage);
        given(dishMapper.dishListToDto(dishesPage)).willReturn(dishesDTO);

        assertEquals(dishService.getDishList(defaultPageRequest, null), dishesDTO);
    }

    @Test
    public void givenName_whenGetDishes_thenReturnDtoList() throws Exception {
        given(dishLiveSearchService.getDishes("Dumplings", 10)).willReturn(dishesDTO);

        assertEquals(dishService.getDishList(defaultPageRequest, "Dumplings"), dishesDTO);
    }
}