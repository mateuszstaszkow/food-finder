package com.foodfinder.dish.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.container.exceptions.rest.ExceptionRestController;
import com.foodfinder.dish.domain.dto.DishDTO;
import com.foodfinder.dish.service.DishService;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.utils.RestControllerTestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DishRestController.class)
@EnableSpringDataWebSupport
public class DishRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DishService dishService;

    @MockBean
    private ExceptionRestController exceptionRestController;

    @Test
    @WithMockUser
    public void givenDish_whenGetDish_thenReturnJson() throws Exception {

        DishDTO dish = DishDTO.builder()
                .id(1L)
                .name("Dumplings")
                .description("Traditional Russian with greaves")
                .products(Collections.singletonList(new ProductDTO()))
                .build();

        given(dishService.getDish(1L)).willReturn(dish);

        mvc.perform(get("/api/dishes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dish.getName())));
    }

    @Test
    @WithMockUser
    public void givenDishes_whenGetDishes_thenReturnJsonArray() throws Exception {

        DishDTO dish = DishDTO.builder()
                .id(1L)
                .name("Dumplings")
                .description("Traditional Russian with greaves")
                .products(Collections.singletonList(new ProductDTO()))
                .build();
        List<DishDTO> allDishes = Collections.singletonList(dish);
        PageRequest defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        given(dishService.getDishList(defaultPageRequest, null)).willReturn(allDishes);

        mvc.perform(get("/api/dishes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(dish.getName())));
    }

    @Test
    @WithMockUser
    public void givenDishesName_whenGetDishes_thenReturnJsonArray() throws Exception {

        DishDTO dish = DishDTO.builder()
                .id(1L)
                .name("Dumplings")
                .description("Traditional Russian with greaves")
                .products(Collections.singletonList(new ProductDTO()))
                .build();
        List<DishDTO> allDishes = Collections.singletonList(dish);
        PageRequest defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        given(dishService.getDishList(defaultPageRequest, "pierogi")).willReturn(allDishes);

        mvc.perform(get("/api/dishes?name=pierogi")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(dish.getName())));
    }

    @Test
    @WithMockUser
    public void givenDish_whenAddDish_thenReturnStatusCreated() throws Exception {

        DishDTO dish = DishDTO.builder()
                .id(1L)
                .name("Dumplings")
                .description("Traditional Russian with greaves")
                .products(Collections.singletonList(new ProductDTO()))
                .build();
        String jsonDish = new ObjectMapper().writeValueAsString(dish);

        mvc.perform(post("/api/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDish))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenUnauthorizedUser_thenReturnStatusUnauthorized() throws Exception {

        mvc.perform(get("/api/dishes"))
                .andExpect(status().isUnauthorized());
    }
}