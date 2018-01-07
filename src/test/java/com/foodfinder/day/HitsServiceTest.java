package com.foodfinder.day;

import com.foodfinder.day.domain.entity.Day;
import com.foodfinder.day.service.HitsService;
import com.foodfinder.dish.repository.DishRepository;
import com.foodfinder.food.domain.mapper.FoodMapper;
import com.foodfinder.food.repository.ProductRepository;
import com.foodfinder.utils.DayBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class HitsServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private DishRepository dishRepository;

    @MockBean
    private FoodMapper foodMapper;

    private HitsService hitsService;

    @Before
    public void setup() {
        hitsService = new HitsService(productRepository, dishRepository, foodMapper);
    }

    @Test
    public void givenDay_whenIncrementHitsForANewDay_thenReturnDayWithIncrementedHits() throws Exception {
        Day day = DayBuilder.getDayEntity();

        long beforeHits = new ArrayList<>(day.getTimedDishes()).get(0).getDish().getHits();
        long afterHits = new ArrayList<>(hitsService.incrementHitsForANewDay(day).getTimedDishes()).get(0)
                .getDish()
                .getHits();

        assertEquals(beforeHits + 1, afterHits);
    }

    @Test
    public void givenDay_whenIncrementHitsForAnUpdatedDay_thenReturnDayWithIncrementedHits() throws Exception {
        Day day = DayBuilder.getDayEntity();

        given(dishRepository.findByIdIn(Collections.singletonList(1L))).willReturn(new ArrayList<>());
        given(productRepository.findByIdIn(new ArrayList<>())).willReturn(new ArrayList<>());

        long beforeHits = new ArrayList<>(day.getTimedDishes()).get(0).getDish().getHits();
        long afterHits = new ArrayList<>(hitsService.incrementHitsForAnUpdatedDay(day).getTimedDishes()).get(0)
                .getDish()
                .getHits();

        assertEquals(beforeHits + 1, afterHits);
    }
}
