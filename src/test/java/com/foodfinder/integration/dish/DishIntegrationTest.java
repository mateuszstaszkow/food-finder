package com.foodfinder.integration.dish;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.dish.domain.dto.DishDTO;
import com.foodfinder.dish.repository.DishRepository;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.integration.config.IntegrationTestSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class DishIntegrationTest extends IntegrationTestSetup {

    @Autowired
    private DishRepository dishRepository;

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void whenGetDishes_thenReturnsStatusOk() throws Exception {
        mockMvc.perform(get("/api/dishes"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz", roles = "VIEW_ADMIN")
    public void givenDish_whenPostDish_thenReturnsStatusCreated() throws Exception {
        DishDTO dish = getDishWithProductsFromDb(1L);
        String jsonDish = new ObjectMapper().writeValueAsString(dish);

        mockMvc.perform(post("/api/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDish))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz", roles = "VIEW_ADMIN")
    public void givenDishWithNullId_whenPostDish_thenReturnsStatusCreated() throws Exception {
        DishDTO dish = getDishWithProductsFromDb(null);
        String jsonDish = new ObjectMapper().writeValueAsString(dish);

        mockMvc.perform(post("/api/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDish))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz", roles = "VIEW_ADMIN")
    public void givenDish_whenGetDish_thenReturnsDish() throws Exception {
        DishDTO dish = getDishWithProductsFromDb(1L);
        String jsonDish = new ObjectMapper().writeValueAsString(dish);

        mockMvc.perform(post("/api/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDish))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/dishes/" + dish.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dish.getName())));
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz", roles = "VIEW_ADMIN")
    public void givenDish_whenGetDishByName_thenReturnsDishArray() throws Exception {
        DishDTO dish = getDishWithProductsFromDb(null);
        String jsonDish = new ObjectMapper().writeValueAsString(dish);

        mockMvc.perform(post("/api/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDish))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/dishes?name=Dump"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(dish.getName())));
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz", roles = "VIEW_ADMIN")
    public void givenProductsAndDish_whenUpdateDish_thenNotAddANewOne() throws Exception {
        addDishToDatabase();

        long before = dishRepository.count();

        String result = mockMvc.perform(get("/api/dishes")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        DishDTO dish = (DishDTO) ((ArrayList)(mapper.readValue(result, new TypeReference<List<DishDTO>>(){}))).get(0);
        String jsonDish = new ObjectMapper().writeValueAsString(dish);

        mockMvc.perform(post("/api/dishes/" + dish.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDish))
                .andExpect(status().isOk());

        assertEquals(before, dishRepository.count());
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz", roles = "VIEW_ADMIN")
    public void givenProductsAndDish_whenUpdateDish_thenUpdateNameField() throws Exception {
        addDishToDatabase();

        String result = mockMvc.perform(get("/api/dishes")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        DishDTO dish = (DishDTO) ((ArrayList)(mapper.readValue(result, new TypeReference<List<DishDTO>>(){}))).get(0);
        dish.setName("Updated dish");
        String jsonDish = new ObjectMapper().writeValueAsString(dish);

        mockMvc.perform(post("/api/dishes/" + dish.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDish))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/dishes/" + dish.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dish.getName())));
    }

    private void addDishToDatabase() throws Exception {
        DishDTO dish = getDishWithProductsFromDb(null);
        String jsonDish = new ObjectMapper().writeValueAsString(dish);

        mockMvc.perform(post("/api/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDish))
                .andExpect(status().isCreated());
    }

    private DishDTO getDishWithProductsFromDb(Long id) throws Exception {
        String result = mockMvc.perform(get("/api/products?page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ProductDTO> products = mapper.readValue(result, new TypeReference<List<ProductDTO>>(){});

        return DishDTO.builder()
                .id(id)
                .name("Dumplings")
                .description("Traditional Russian with greaves")
                .products(products)
                .build();
    }
}