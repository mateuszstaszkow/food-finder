package com.foodfinder.integration.day;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.integration.config.IntegrationTestSetup;
import com.foodfinder.utils.DayBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class DayIntegrationTest extends IntegrationTestSetup {

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void whenGetDays_thenReturnsStatusOk() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void whenGetPaginatedDays_thenReturnsArray() throws Exception {
        mockMvc.perform(get("/api/products?page=0&size=" + 10)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)));
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenProductsAndDay_whenGetDay_thenReturnsStatusCreated() throws Exception {
        givenProductsAndDay_whenGetDay_thenReturnsStatusCreated(1L, 1L);
        givenProductsAndDay_whenGetDay_thenReturnsStatusCreated(null, null);
        givenProductsAndDay_whenGetDay_thenReturnsStatusCreated(3L, null);
        givenProductsAndDay_whenGetDay_thenReturnsStatusCreated(null, 1L);
    }

    private void givenProductsAndDay_whenGetDay_thenReturnsStatusCreated(Long dayId, Long dishId) throws Exception {
        String result = mockMvc.perform(get("/api/products?page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ProductDTO> products = mapper.readValue(result, new TypeReference<List<ProductDTO>>(){});
        DayDTO day = DayBuilder.getDayDTO(products, dayId, dishId);
        String jsonDay = new ObjectMapper().writeValueAsString(day);

        mockMvc.perform(post("/api/days")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDay))
                .andExpect(status().isCreated());
    }

    public void givenProductsAndDay_whenGetDay_thenReturnsDay() throws Exception {
        String result = mockMvc.perform(get("/api/products?page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ProductDTO> products = mapper.readValue(result, new TypeReference<List<ProductDTO>>(){});
        DayDTO day = DayBuilder.getDayDTO(products, 1L, 1L);
        String jsonDay = new ObjectMapper().writeValueAsString(day);

        mockMvc.perform(post("/api/days")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDay))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/days/" + day.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(day.getName())))
                .andExpect(jsonPath("$.timedDishes[0].dish.name",
                        is(day.getTimedDishes().get(0).getDish().getName())))
                .andExpect(jsonPath("$.timedDishes[0].dish.products[0].fat.name",
                        is(products.get(0).getFat().getName())));
    }
}
